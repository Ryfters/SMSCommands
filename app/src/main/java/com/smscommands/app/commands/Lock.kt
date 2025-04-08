package com.smscommands.app.commands

import android.app.admin.DevicePolicyManager
import android.content.Context
import com.smscommands.app.R
import com.smscommands.app.permissions.Permission

class Lock : Command {
    override val id = "command_lock"
    override val label = R.string.command_lock_label
    override val description = R.string.command_lock_desc
    override val usage = R.string.command_lock_usage

    override val requiredPermissions = listOf(
        Permission.ADMIN
    )

    override fun onReceive(context: Context, args: List<String>, sender: String, onReply: (String) -> Unit) {
        val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        devicePolicyManager.lockNow()

        onReply(context.getString(R.string.command_lock_reply_success))

    }
}