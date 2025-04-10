package com.smscommands.app.commands

import android.content.Context
import android.content.Intent
import androidx.camera.core.ImageCapture
import com.smscommands.app.R
import com.smscommands.app.commands.CaptureActivity.Companion.CAMERA_BACK
import com.smscommands.app.commands.CaptureActivity.Companion.CAMERA_BOTH
import com.smscommands.app.commands.CaptureActivity.Companion.CAMERA_EXTRA
import com.smscommands.app.commands.CaptureActivity.Companion.CAMERA_FRONT
import com.smscommands.app.commands.CaptureActivity.Companion.FLASH_MODE_EXTRA
import com.smscommands.app.commands.Command.Companion.SENDER_EXTRA
import com.smscommands.app.commands.params.ChoiceParamDefinition
import com.smscommands.app.permissions.Permission

class Capture : Command {
    override val id = "command_capture"
    override val label = R.string.command_capture_label
    override val description = R.string.command_capture_desc
    override val usage = R.string.command_capture_usage

    override val requiredPermissions = listOf(
        Permission.OVERLAY,
        Permission.CAMERA
    )


    override val params = mapOf(
        FLASH_PARAM to ChoiceParamDefinition(
            R.string.command_capture_param_flash,
            R.string.command_capture_param_flash_desc,
            defaultValue = ImageCapture.FLASH_MODE_OFF,
            choices = flashParamChoices
        ),
        CAMERA_PARAM to ChoiceParamDefinition(
            R.string.command_capture_param_camera,
            R.string.command_capture_param_camera_desc,
            defaultValue = CAMERA_BOTH,
            choices = cameraParamChoices
        )
    )

    override fun onReceive(context: Context, parameters: Map<String, Any>, sender: String, onReply: (String) -> Unit) {
        val flashMode = parameters[FLASH_PARAM] as Int
        val camera = parameters[CAMERA_PARAM] as Int


//        val flashMode = parseFlashMode(context, args)
//            ?: return onReply(context.getString(R.string.sms_reply_parameter_unknown, context.getString(R.string.command_capture_param_flash)))
//
//        val camera = parseCamera(context, args)
//            ?: return onReply(context.getString(R.string.sms_reply_parameter_unknown, context.getString(R.string.command_capture_param_camera)))

        val intent = Intent(context, CaptureActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NO_ANIMATION

            putExtra(SENDER_EXTRA, sender)
            putExtra(FLASH_MODE_EXTRA, flashMode)
            putExtra(CAMERA_EXTRA, camera)
        }

        context.startActivity(intent)
    }

    companion object {
        private val flashParamChoices = mapOf(
            R.string.common_on to ImageCapture.FLASH_MODE_ON,
            R.string.common_off to ImageCapture.FLASH_MODE_OFF,
            R.string.common_auto to ImageCapture.FLASH_MODE_AUTO,
        )
        private val cameraParamChoices = mapOf(
            R.string.command_capture_param_camera_front to CAMERA_FRONT,
            R.string.command_capture_param_camera_back to CAMERA_BACK,
            R.string.command_capture_param_camera_both to CAMERA_BOTH,
        )
        private const val CAMERA_PARAM = "camera"
        private const val FLASH_PARAM = "flash"
    }
}