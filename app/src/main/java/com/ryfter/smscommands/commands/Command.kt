package com.ryfter.smscommands.commands

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.ryfter.smscommands.commands.params.ParamsDefinition
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.permissions.Permission
import com.ryfter.smscommands.ui.navigation.MyNavBackStack

interface Command {
    val id: String

    @get:StringRes
    val label: Int

    @get:StringRes
    val description: Int

    val requiredPermissions: List<Permission>

    val params: Map<String, ParamsDefinition>
        get() = emptyMap()

    fun onReceive(
        context: Context,
        parameters: Map<String, Any?>,
        sender: String,
        id: Long?
    )

    // Using a lambda so i can check if it's not null without invoking it
    val extraContent: (@Composable ColumnScope.(MyNavBackStack, UiStateViewModel) -> Unit)?
        get() = null

    companion object {
        const val SENDER_EXTRA = "sender"
        const val ID_EXTRA = "id"

        val CALL = Call()
        val CAPTURE = Capture()
        val GPS = Gps()
        val HELP = Help()
        val LOCK = Lock()
        val RING = Ring()

        val LIST = listOf(
            CALL,
            CAPTURE,
            GPS,
            HELP,
            LOCK,
            RING,
        )
    }
}
