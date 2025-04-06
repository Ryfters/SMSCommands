package com.smscommands.app.commands

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.SmsManager
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.concurrent.futures.await
import androidx.lifecycle.lifecycleScope
import com.smscommands.app.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume

class CaptureActivity : ComponentActivity() {
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider

    var sender: String? = null
    var flashMode: Int? = null
    var camera: Int? = null

    lateinit var onReply: (String) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        sender = intent.getStringExtra(FLASH_MODE_EXTRA)

        camera = intent.getIntExtra(CAMERA_EXTRA, CAMERA_BOTH)
        flashMode = intent.getIntExtra(FLASH_MODE_EXTRA, ImageCapture.FLASH_MODE_OFF)

        enableEdgeToEdge()

        onReply = { message ->
            val smsManager: SmsManager = this.getSystemService(SmsManager::class.java)
            smsManager.sendTextMessage(sender, null, message, null, null)
        }

    }

    override fun onResume() {
        super.onResume()
        val captureMode = ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
        val flashMode = ImageCapture.FLASH_MODE_OFF

        var attempts = mutableListOf<Boolean>()

        lifecycleScope.launch {
            cameraProvider = ProcessCameraProvider.getInstance(applicationContext).await()

            if (camera == CAMERA_BACK || camera == CAMERA_BOTH) {
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                attempts.add(
                    takePhoto(cameraSelector, captureMode, flashMode)
                )
            }

            if (camera == CAMERA_FRONT || camera == CAMERA_BOTH) {
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                attempts.add(
                    takePhoto(cameraSelector, captureMode, flashMode)
                )
            }

            processResults(attempts)

            finishAndRemoveTask()
        }
    }

    private suspend fun takePhoto(cameraSelector: CameraSelector, captureMode: Int, flashMode: Int): Boolean {

        val imageCapture = ImageCapture.Builder()
            .setCaptureMode(captureMode)
            .setFlashMode(flashMode)
            .build()

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture)

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault()).format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${getString(R.string.app_name)}") // TODO: Maybe localize this
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        return suspendCancellableCoroutine<Boolean> { continuation ->
            imageCapture.takePicture(
                outputOptions,
                cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        continuation.resume(true)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        continuation.resume(false)
                    }
                }
            )
        }
    }

    private fun processResults(attempts: MutableList<Boolean>) {
        val successCount = attempts.count { it }.toString()
        val failureCount = attempts.count { !it }.toString()

        onReply(getString(R.string.command_capture_reply_success, successCount, failureCount))

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraProvider.unbindAll()
        cameraExecutor.shutdown()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0)
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val CAMERA_FRONT = 1
        const val CAMERA_BACK = 2
        const val CAMERA_BOTH = 0

        const val SENDER_EXTRA = "sender"
        const val CAMERA_EXTRA = "camera"
        const val FLASH_MODE_EXTRA = "flash_mode"
    }
}