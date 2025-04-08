package com.smscommands.app.commands

import android.content.Context
import android.content.Intent
import android.media.AudioDeviceInfo
import android.media.AudioManager
import androidx.core.net.toUri
import com.smscommands.app.R
import com.smscommands.app.permissions.Permission
import com.smscommands.app.utils.getArgumentValue

class Call : Command {
    override val id = "command_call"
    override val label = R.string.command_call_label
    override val desc = R.string.command_call_desc
    override val usage = R.string.command_call_usage

    override val requiredPermissions = listOf(
        Permission.PHONE,
        Permission.OVERLAY,
    )
    override fun onReceive(context: Context, args: List<String>, sender: String, onReply: (String) -> Unit) {

        val audioMode = parseAudioMode(context, args)
            ?: return onReply(context.getString(R.string.sms_reply_parameter_unknown, context.getString(R.string.command_capture_param_camera)))

        val intent = Intent(Intent.ACTION_CALL).apply {
            data = "tel:$sender".toUri()
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)

        var reply = context.getString(R.string.command_call_reply_success, sender)
        onReply(reply)
        // TODO: add audioMode info

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

    private fun parseAudioMode(context: Context, args: List<String>): String? {
        val paramName = context.getString(R.string.command_call_param_audio)
        val paramValue = getArgumentValue(args, paramName)
        val paramOptions = mapOf(
            context.getString(R.string.command_call_param_audio_speaker) to SPEAKER,
            context.getString(R.string.command_call_param_audio_mute) to MUTE,
            null to NONE
        )

        return paramOptions[paramValue]
    }

    companion object {
        private const val SPEAKER = "audiomode_speaker"
        private const val MUTE = "audiomode_mute"
        private const val NONE = "audiomode_none"

    }
}