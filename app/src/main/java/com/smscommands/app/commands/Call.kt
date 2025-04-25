package com.smscommands.app.commands

import android.content.Context
import android.content.Intent
import android.media.AudioDeviceInfo
import android.media.AudioManager
import androidx.core.net.toUri
import com.smscommands.app.R
import com.smscommands.app.commands.params.ChoiceParamDefinition
import com.smscommands.app.permissions.Permission

class Call : Command {
    override val id = "command_call"
    override val label = R.string.command_call_label
    override val description = R.string.command_call_desc
    override val usage = R.string.command_call_usage

    override val requiredPermissions = listOf(
        Permission.PHONE,
        Permission.OVERLAY,
    )


    private val audioParamChoices = mapOf<Any, Int>(
        SPEAKER to R.string.command_call_param_audio_speaker,
        MUTE to R.string.command_call_param_audio_mute,
    )

    override val params = mapOf(
        AUDIO_PARAM to ChoiceParamDefinition(
            name = R.string.command_call_param_audio,
            desc = R.string.command_call_param_audio_desc,
            defaultValue = NONE,
            choices = audioParamChoices
        )
    )

    override fun onReceive(context: Context, parameters: Map<String, Any?>, sender: String, onReply: (String) -> Unit) {
        val audioMode = parameters[AUDIO_PARAM] as String


        val intent = Intent(Intent.ACTION_CALL).apply {
            data = "tel:$sender".toUri()
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)

        var reply = context.getString(R.string.command_call_reply_success, sender)
        onReply(reply)
        // TODO: add audioMode info?

        if (audioMode != NONE) {
            var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            if (audioMode == SPEAKER) { // TODO: Test this on real device with earpiece
                val audioDevices = audioManager.availableCommunicationDevices.filter { it.isSink }
                val speaker = audioDevices.find { it.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER }
                speaker?.let {
                    audioManager.setCommunicationDevice(speaker)
                }

                val voiceCallMaxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
                audioManager.setStreamVolume(
                    AudioManager.STREAM_VOICE_CALL,
                    voiceCallMaxVol,
                    0
                )
            }

            if (audioMode == MUTE) { // TODO: Test this too
                audioManager.setStreamVolume(
                    AudioManager.STREAM_VOICE_CALL,
                    0,
                    0
                )
            }
        }
    }

    companion object {
        private const val SPEAKER = "audiomode_speaker"
        private const val MUTE = "audiomode_mute"
        private const val NONE = "audiomode_none"

        const val AUDIO_PARAM = "audio"
    }
}