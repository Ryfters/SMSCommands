package com.ryfter.smscommands.commands

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.concurrent.futures.await
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.klinker.android.send_message.Message
import com.klinker.android.send_message.Settings
import com.klinker.android.send_message.Transaction
import com.ryfter.smscommands.R
import com.ryfter.smscommands.commands.Command.Companion.ID_EXTRA
import com.ryfter.smscommands.commands.Command.Companion.SENDER_EXTRA
import com.ryfter.smscommands.data.SyncPreferences
import com.ryfter.smscommands.utils.reply
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume


@Suppress("SameParameterValue")
class CaptureActivity : ComponentActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider

    private lateinit var settings: Settings
    private lateinit var transaction: Transaction

    private var syncPreferences: SyncPreferences? = null

    private var id: Long? = null
    private var sender: String? = null
    private var flashMode: Int? = null

    private lateinit var _reply: (String) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()

        id = intent.getLongExtra(ID_EXTRA, -1L).takeIf { it != -1L }

        sender = intent.getStringExtra(SENDER_EXTRA) as String

        flashMode = intent.getIntExtra(FLASH_MODE_EXTRA, FLASH_MODE_OFF)

        id?.let { syncPreferences = SyncPreferences.getPreferences(applicationContext) }

        _reply = { message ->
            reply(applicationContext, message, sender.toString(), id)
        }

    }

    override fun onResume() {
        super.onResume()

        settings = Settings()
        settings.useSystemSending = true

        transaction = Transaction(applicationContext, settings)

        lifecycleScope.launch {
            cameraProvider = ProcessCameraProvider.getInstance(applicationContext).await()

            val cameras = cameraProvider.availableCameraInfos
            val cameraCount = cameras.size

            val jobs: MutableList<Job> = mutableListOf()

            cameras.mapIndexed { index, camInfo ->
                try {
                    val uri = takePhoto(camInfo.cameraSelector)
                    jobs.add(launch { sendPhoto(uri, index, cameraCount) })
                } catch (e: Exception) {
                    Log.w(TAG, "Photo capture failed: ${e.message}", e)
                    _reply(getString(R.string.command_capture_reply_error, index + 1, cameraCount))
                }
            }

            jobs.joinAll()

            finishAndRemoveTask()
        }
    }

    private suspend fun takePhoto(
        cameraSelector: CameraSelector,
    ): Uri {

        val resolutionSelector = ResolutionSelector.Builder()
            .setResolutionStrategy(
                ResolutionStrategy(
                    Size(1920, 1440),
                    ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
                )
            ).setAspectRatioStrategy(
                AspectRatioStrategy(RATIO_4_3, AspectRatioStrategy.FALLBACK_RULE_AUTO)
            ).build()

        val imageCapture = ImageCapture.Builder()
            .setFlashMode(flashMode ?: FLASH_MODE_OFF)
            .setResolutionSelector(resolutionSelector)
            .build()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture).apply {
            cameraControl.setZoomRatio(cameraInfo.zoomState.value?.minZoomRatio ?: 1f)
            // cameraInfo.intrinsicZoomRatio to get zoom level
        }

        val fileDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            getString(R.string.app_name)
        ).apply { mkdirs() }

        val fileName = SimpleDateFormat(
            FILENAME_FORMAT,
            Locale.getDefault()
        ).format(System.currentTimeMillis())

        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(File(fileDir, "$fileName.jpg")).build()

        return suspendCancellableCoroutine { continuation ->
            imageCapture.takePicture(
                outputOptions,
                cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        if (outputFileResults.savedUri == null) throw IllegalStateException("Couldn't save image")
                        return continuation.resume(outputFileResults.savedUri!!)
                    }
                    override fun onError(exception: ImageCaptureException) {
                        throw exception
                    }
                }
            )
        }
    }

    private suspend fun sendPhoto(uri: Uri, index: Int, camCount: Int) {

        var image = uri.toFile()


        while (image.length() > 300_000) {

            image = Compressor.compress(applicationContext, image) {
                quality(80)
                size(300_000)
            }
        }

        val message = Message(
            getString(R.string.command_capture_reply_success, index + 1, camCount),
            sender.toString()
        )

        applicationContext.contentResolver.openInputStream(image.toUri())?.use {
            val bytes = it.readBytes()
            val mimeType = "image/jpeg"
            val name = image.name
            message.addMedia(bytes, mimeType, name, name)
        }

        message.save = false
        message.messageUri =
            "content://mms/outbox/".toUri() // not sure what i should put here but nothing makes it crash

        transaction.sendNewMessage(message)

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraProvider.unbindAll()
        cameraExecutor.shutdown()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0)
    }

    companion object {
        const val TAG = "CaptureActivity"

        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmssSSS"

        const val FLASH_MODE_EXTRA = "flash_mode"
    }
}