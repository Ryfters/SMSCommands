package com.ryfter.smscommands.commands

import android.content.Context
import android.content.Intent
import androidx.camera.core.ImageCapture
import com.ryfter.smscommands.R
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
        Permission.CAMERA,
        Permission.PHONE // Required for sending MMS
    )


    private val flashParamChoices = mapOf<Any, Int>(
        ImageCapture.FLASH_MODE_ON to R.string.common_on,
        ImageCapture.FLASH_MODE_OFF to R.string.common_off,
        ImageCapture.FLASH_MODE_AUTO to R.string.common_auto,
    )

    override val params = mapOf(
        FLASH_PARAM to ChoiceParamDefinition(
            name = R.string.command_capture_param_flash,
            desc = R.string.command_capture_param_flash_desc,
            defaultValue = ImageCapture.FLASH_MODE_OFF,
            choices = flashParamChoices
        ),
    )

    override fun onReceive(
        context: Context,
        parameters: Map<String, Any?>,
        sender: String,
        id: Long?
    ) {

        val flashMode = parameters[FLASH_PARAM] as Int

        val intent = Intent(context, CaptureActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NO_ANIMATION

            putExtra(ID_EXTRA, id)
            putExtra(SENDER_EXTRA, sender)
            putExtra(FLASH_MODE_EXTRA, flashMode)
        }

        context.startActivity(intent)
    }

    companion object {
        private const val FLASH_PARAM = "flash"
    }
}