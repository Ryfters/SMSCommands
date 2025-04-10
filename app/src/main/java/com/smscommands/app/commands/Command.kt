package com.smscommands.app.commands

import android.content.Context
import androidx.annotation.StringRes
import com.smscommands.app.commands.params.ParamsDefinition
import com.smscommands.app.permissions.Permission

interface Command {
    val id: String

    @get:StringRes
    val label: Int

    @get:StringRes
    val description: Int

    @get:StringRes
    val usage: Int

    val requiredPermissions: List<Permission>

    val params: Map<String, ParamsDefinition>
        get() = emptyMap()

    fun onReceive(context: Context, parameters: Map<String, Any>, sender: String, onReply: (String) -> Unit, )

    companion object {
        const val SENDER_EXTRA = "sender"

        val CALL = Call()
        val CAPTURE = Capture()
        val GPS = Gps()
        val LOCK = Lock()
        val RING = Ring()

        val LIST = listOf(
            CALL,
            CAPTURE,
            GPS,
            LOCK,
            RING,
        )
    }
}
