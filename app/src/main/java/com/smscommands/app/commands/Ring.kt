package com.smscommands.app.commands

import android.content.Context
import android.content.Intent
import com.smscommands.app.R
import com.smscommands.app.commands.RingActivity.Companion.RING_TIME_EXTRA
import com.smscommands.app.commands.RingActivity.Companion.STOP_RINGING_INTENT
import com.smscommands.app.permissions.Permission
import com.smscommands.app.utils.containsArgument
import com.smscommands.app.utils.getArgumentValue

class Ring : Command {
    override val id = "command_ring"
    override val label = R.string.command_ring_label
    override val description = R.string.command_ring_desc
    override val usage = R.string.command_ring_usage

    override val requiredPermissions = listOf(
        Permission.OVERLAY,
    )

    override fun onReceive(context: Context, args: List<String>, sender: String, onReply: (String) -> Unit) {
        val intent = Intent(context, RingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        }

        if (containsArgument(args, context.getString(R.string.common_stop))) {
                val stopIntent = Intent(STOP_RINGING_INTENT)
                context.sendBroadcast(stopIntent)
                onReply(context.getString(R.string.command_ring_reply_stopped))
                return
        }

        var timeInS = 120
        
        getArgumentValue(args, context.getString(R.string.command_ring_param_time))?.toLongOrNull()?.let {
                val timeInMs = it * 1000L
                intent.putExtra(RING_TIME_EXTRA, timeInMs)
                timeInS = it.toInt()
        }

        context.startActivity(intent)
        
        onReply(context.getString(R.string.ringing_device_for_seconds, timeInS))
    }
}
