package com.ryfter.smscommands.commands

import android.content.Context
import android.content.Intent
import com.ryfter.smscommands.R
import com.ryfter.smscommands.commands.RingActivity.Companion.STOP_RINGING_INTENT
import com.ryfter.smscommands.commands.params.FlagParamDefinition
import com.ryfter.smscommands.commands.params.IntParamDefinition
import com.ryfter.smscommands.permissions.Permission
import com.ryfter.smscommands.utils.reply

class Ring : Command {
    override val id = "command_ring"
    override val label = R.string.command_ring_label
    override val description = R.string.command_ring_desc

    override val requiredPermissions = listOf(
        Permission.OVERLAY,
    )

    override val params = mapOf(
        TIME_PARAM to IntParamDefinition(
            name = R.string.command_ring_param_time,
            desc = R.string.command_ring_param_time_desc,
            defaultValue = 120,
        ),
        STOP_PARAM to FlagParamDefinition(
            name = R.string.common_stop,
            desc = R.string.command_ring_param_stop_desc,
        )
    )

    override fun onReceive(
        context: Context,
        parameters: Map<String, Any?>,
        sender: String,
        id: Long?
    ) {

        val timeInS = parameters[TIME_PARAM] as Int
        val stop = parameters[STOP_PARAM] as Boolean

        if (stop) {
            val stopIntent = Intent(STOP_RINGING_INTENT)
            context.sendBroadcast(stopIntent)
            reply(context, context.getString(R.string.command_ring_reply_stopped), sender, id)
            return
        }

        val intent = Intent(context, RingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            putExtra(RingActivity.RING_TIME_EXTRA, timeInS * 1_000L)
        }


        context.startActivity(intent)

        reply(context, context.getString(R.string.command_ring_reply_started, timeInS), sender, id)
    }

    companion object {
        const val STOP_PARAM = "stop"
        const val TIME_PARAM = "time"
    }
}
