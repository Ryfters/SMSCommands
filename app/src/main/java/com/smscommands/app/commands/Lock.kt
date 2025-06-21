package com.smscommands.app.commands

import android.app.admin.DevicePolicyManager
import android.content.Context
import com.smscommands.app.R
import com.smscommands.app.permissions.Permission
import com.smscommands.app.utils.reply

class Lock : Command {
    override val id = "command_lock"
    override val label = R.string.command_lock_label
    override val description = R.string.command_lock_desc

    override val requiredPermissions = listOf(
        Permission.ADMIN
    )

    override fun onReceive(
        context: Context,
        parameters: Map<String, Any?>,
        sender: String,
        id: Long?
    ) {

        val devicePolicyManager = context.getSystemService(DevicePolicyManager::class.java)
        devicePolicyManager.lockNow()
        reply(context, context.getString(R.string.command_lock_reply_success), sender, id)
    }
}