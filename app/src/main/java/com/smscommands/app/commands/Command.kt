package com.smscommands.app.commands

import android.content.Context
import androidx.annotation.StringRes
import com.smscommands.app.permissions.Permission

interface Command {
    val id: String

    @get:StringRes
    val label: Int

    @get:StringRes
    val desc: Int

    @get:StringRes
    val usage: Int

    val requiredPermissions: List<Permission>

    fun onReceive(context: Context, args: List<String>, sender: String, onReply: (String) -> Unit, )

    companion object {
        const val SENDER_EXTRA = "sender"
    }
}
