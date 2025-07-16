package com.ryfter.smscommands.commands

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.concurrent.futures.await
import androidx.lifecycle.lifecycleScope
import com.ryfter.smscommands.R
import com.ryfter.smscommands.commands.Command.Companion.ID_EXTRA
import com.ryfter.smscommands.commands.Command.Companion.SENDER_EXTRA
import com.ryfter.smscommands.data.SyncPreferences
import com.ryfter.smscommands.utils.reply
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume

@Suppress("SameParameterValue")
class CaptureActivity : ComponentActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider

    private var syncPreferences: SyncPreferences? = null

    private var id: Long? = null
    private var sender: String? = null
    private var flashMode: Int? = null
    private var camera: Int? = null

    private lateinit var _reply: (String) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        id = intent.getLongExtra(ID_EXTRA, -1L).takeIf { it != -1L }

        sender = intent.getStringExtra(SENDER_EXTRA) as String

        camera = intent.getIntExtra(CAMERA_EXTRA, CAMERA_BOTH)
        flashMode = intent.getIntExtra(FLASH_MODE_EXTRA, ImageCapture.FLASH_MODE_OFF)


        id?.let { syncPreferences = SyncPreferences.getPreferences(applicationContext) }

        _reply = { message ->
            reply(applicationContext, message, sender.toString(), id)
        }

    }

    override fun onResume() {
        super.onResume()
        val captureMode = ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
        val flashMode = ImageCapture.FLASH_MODE_OFF

        lifecycleScope.launch {
            cameraProvider = ProcessCameraProvider.getInstance(applicationContext).await()

            if (camera == CAMERA_BACK || camera == CAMERA_BOTH) {
                takePhoto(CAMERA_BACK, captureMode, flashMode)
            }

            if (camera == CAMERA_FRONT || camera == CAMERA_BOTH) {
                takePhoto(CAMERA_FRONT, captureMode, flashMode)
            }

            finishAndRemoveTask()
        }
    }

    private suspend fun takePhoto(
        camera: Int,
        captureMode: Int,
        flashMode: Int
    ) {
        val cameraSelector: CameraSelector
        val cameraLabel: String

        when (camera) {
            CAMERA_FRONT -> {
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                cameraLabel = getString(R.string.command_capture_camera_front)

            }
            CAMERA_BACK -> {
                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraLabel = getString(R.string.command_capture_camera_back)
            }
            else -> throw Exception("Invalid camera in takePhoto()")
        }

        val imageCapture = ImageCapture.Builder()
            .setCaptureMode(captureMode)
            .setFlashMode(flashMode)
            .build()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture)

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault()).format(System.currentTimeMillis())

        val fileDir = "${Environment.DIRECTORY_PICTURES}/${this.getString(R.string.app_name)}"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, fileDir)
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        return suspendCancellableCoroutine { continuation ->
            imageCapture.takePicture(
                outputOptions,
                cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        _reply(getString(R.string.command_capture_reply_success, cameraLabel))

                        continuation.resume(Unit)
                    }
                    override fun onError(exception: ImageCaptureException) {
                        _reply(getString(R.string.command_capture_reply_error, cameraLabel))
                        continuation.resume(Unit)
                    }
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraProvider.unbindAll()
        cameraExecutor.shutdown()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0)
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmssSSS"
        const val CAMERA_FRONT = 1
        const val CAMERA_BACK = 2
        const val CAMERA_BOTH = 0

        const val CAMERA_EXTRA = "camera"
        const val FLASH_MODE_EXTRA = "flash_mode"
    }
}