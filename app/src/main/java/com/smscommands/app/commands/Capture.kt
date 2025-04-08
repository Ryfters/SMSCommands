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
import com.smscommands.app.permissions.Permission
import com.smscommands.app.utils.getArgumentValue

class Capture : Command {
    override val id = "command_capture"
    override val label = R.string.command_capture_label
    override val description = R.string.command_capture_desc
    override val usage = R.string.command_capture_usage

    override val requiredPermissions = listOf(
        Permission.OVERLAY,
        Permission.CAMERA
    )

    override fun onReceive(context: Context, args: List<String>, sender: String, onReply: (String) -> Unit) {

        val flashMode = parseFlashMode(context, args)
            ?: return onReply(context.getString(R.string.sms_reply_parameter_unknown, context.getString(R.string.command_capture_param_flash)))

        val camera = parseCamera(context, args)
            ?: return onReply(context.getString(R.string.sms_reply_parameter_unknown, context.getString(R.string.command_capture_param_camera)))

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

    private fun parseFlashMode(context: Context, args: List<String>): Int? {
        val paramName = context.getString(R.string.command_capture_param_flash)
        val paramValue = getArgumentValue(args, paramName)
        val validParams = mapOf(
            context.getString(R.string.common_on) to ImageCapture.FLASH_MODE_ON,
            context.getString(R.string.common_off) to ImageCapture.FLASH_MODE_OFF,
            context.getString(R.string.common_auto) to ImageCapture.FLASH_MODE_AUTO,
            null to ImageCapture.FLASH_MODE_OFF
        )

        return validParams[paramValue]
    }

    private fun parseCamera(context: Context, args: List<String>): Int? {
        val paramName = context.getString(R.string.command_capture_param_camera)
        val paramValue = getArgumentValue(args, paramName)
        val validParams = mapOf(
            context.getString(R.string.command_capture_param_camera_front) to CAMERA_FRONT,
            context.getString(R.string.command_capture_param_camera_back) to CAMERA_BACK,
            context.getString(R.string.command_capture_param_camera_both) to CAMERA_BOTH,
            null to CAMERA_BOTH
        )
        return validParams[paramValue]
    }

}