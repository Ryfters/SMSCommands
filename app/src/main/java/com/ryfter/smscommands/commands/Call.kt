package com.ryfter.smscommands.commands

import android.content.Context
import android.content.Intent
import android.media.AudioDeviceInfo
import android.media.AudioManager
import androidx.core.net.toUri
import com.ryfter.smscommands.R
import com.ryfter.smscommands.commands.params.ChoiceParamDefinition
import com.ryfter.smscommands.permissions.Permission
import com.ryfter.smscommands.utils.reply

class Call : Command {
    override val id = "command_call"
    override val label = R.string.command_call_label
    override val description = R.string.command_call_desc

    override val requiredPermissions = listOf(
        Permission.PHONE,
        Permission.OVERLAY,
    )


    private val volParamChoices = mapOf<Any, Int>(
        MAX to R.string.command_call_param_vol_max,
        MIN to R.string.command_call_param_vol_min,
    )

    override val params = mapOf(
        VOL_PARAM to ChoiceParamDefinition(
            name = R.string.command_call_param_vol,
            desc = R.string.command_call_param_vol_desc,
            choices = volParamChoices
        )
    )

    override fun onReceive(
        context: Context,
        parameters: Map<String, Any?>,
        sender: String,
        id: Long?
    ) {

        val vol = parameters[VOL_PARAM] as String?

        val intent = Intent(Intent.ACTION_CALL).apply {
            data = "tel:$sender".toUri()
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)

        val reply = context.getString(R.string.command_call_reply_success, sender)
        reply(context, reply, sender, id)

        if (vol != null) {
            val audioManager = context.getSystemService(AudioManager::class.java)

            if (vol == MAX) {
                val audioDevices =
                    audioManager.availableCommunicationDevices.filter { it.isSink } // sink is output
                val speaker = audioDevices.find { it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER }
                speaker?.let { audioManager.setCommunicationDevice(speaker) }

                val voiceCallMaxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)

                audioManager.setStreamVolume(
                    AudioManager.STREAM_VOICE_CALL,
                    voiceCallMaxVol,
                    0
                )
            }

            if (vol == MIN) {
                val voiceCallMinVol =
                    audioManager.getStreamMinVolume(AudioManager.STREAM_VOICE_CALL)
                audioManager.setStreamVolume(
                    AudioManager.STREAM_VOICE_CALL,
                    voiceCallMinVol,
                    0
                )
            }
        }
    }

    companion object {
        private const val MAX = "vol_min"
        private const val MIN = "vol_max"

        const val VOL_PARAM = "vol"
    }
}