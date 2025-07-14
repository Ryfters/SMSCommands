package com.ryfter.smscommands.commands

import android.content.Context
import android.content.Intent
import androidx.camera.core.ImageCapture
import com.ryfter.smscommands.R
import com.ryfter.smscommands.commands.CaptureActivity.Companion.CAMERA_BACK
import com.ryfter.smscommands.commands.CaptureActivity.Companion.CAMERA_BOTH
import com.ryfter.smscommands.commands.CaptureActivity.Companion.CAMERA_EXTRA
import com.ryfter.smscommands.commands.CaptureActivity.Companion.CAMERA_FRONT
import com.ryfter.smscommands.commands.CaptureActivity.Companion.FLASH_MODE_EXTRA
import com.ryfter.smscommands.commands.Command.Companion.ID_EXTRA
import com.ryfter.smscommands.commands.Command.Companion.SENDER_EXTRA
import com.ryfter.smscommands.commands.params.ChoiceParamDefinition
import com.ryfter.smscommands.permissions.Permission

class Capture : Command {
    override val id = "command_capture"
    override val label = R.string.command_capture_label
    override val description = R.string.command_capture_desc

    override val requiredPermissions = listOf(
        Permission.OVERLAY,
        Permission.CAMERA
    )


    private val flashParamChoices = mapOf<Any, Int>(
        ImageCapture.FLASH_MODE_ON to R.string.common_on,
        ImageCapture.FLASH_MODE_OFF to R.string.common_off,
        ImageCapture.FLASH_MODE_AUTO to R.string.common_auto,
    )
    private val cameraParamChoices = mapOf<Any, Int>(
        CAMERA_FRONT to R.string.command_capture_param_camera_front,
        CAMERA_BACK to R.string.command_capture_param_camera_back,
        CAMERA_BOTH to R.string.command_capture_param_camera_both,
    )

    override val params = mapOf(
        FLASH_PARAM to ChoiceParamDefinition(
            name = R.string.command_capture_param_flash,
            desc = R.string.command_capture_param_flash_desc,
            defaultValue = ImageCapture.FLASH_MODE_OFF,
            choices = flashParamChoices
        ),
        CAMERA_PARAM to ChoiceParamDefinition(
            name = R.string.command_capture_param_camera,
            desc = R.string.command_capture_param_camera_desc,
            defaultValue = CAMERA_BOTH,
            choices = cameraParamChoices
        )
    )

    override fun onReceive(
        context: Context,
        parameters: Map<String, Any?>,
        sender: String,
        id: Long?
    ) {

        val flashMode = parameters[FLASH_PARAM] as Int
        val camera = parameters[CAMERA_PARAM] as Int

        val intent = Intent(context, CaptureActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NO_ANIMATION

            putExtra(ID_EXTRA, id)
            putExtra(SENDER_EXTRA, sender)
            putExtra(FLASH_MODE_EXTRA, flashMode)
            putExtra(CAMERA_EXTRA, camera)
        }

        context.startActivity(intent)
    }

    companion object {
        private const val CAMERA_PARAM = "camera"
        private const val FLASH_PARAM = "flash"
    }
}