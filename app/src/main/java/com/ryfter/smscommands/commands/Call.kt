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


    private val audioModeParamChoices = mapOf<Any, Int>(
        SPEAKER to R.string.command_call_param_audio_speaker,
        DEAFEN to R.string.command_call_param_audio_deafen,
    )

    override val params = mapOf(
        MODE_PARAM to ChoiceParamDefinition(
            name = R.string.command_call_param_mode,
            desc = R.string.command_call_param_audio_desc,
            choices = audioModeParamChoices
        )
    )

    override fun onReceive(
        context: Context,
        parameters: Map<String, Any?>,
        sender: String,
        id: Long?
    ) {

        val audioMode = parameters[MODE_PARAM] as String?

        val intent = Intent(Intent.ACTION_CALL).apply {
            data = "tel:$sender".toUri()
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)

        var reply = context.getString(R.string.command_call_reply_success, sender)
        reply(context, reply, sender, id)

        if (audioMode != null) {
            var audioManager = context.getSystemService(AudioManager::class.java)

            if (audioMode == SPEAKER) {
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

            if (audioMode == DEAFEN) {
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
        private const val SPEAKER = "mode_speaker"
        private const val DEAFEN = "mode_deafen"

        const val MODE_PARAM = "audio"
    }
}