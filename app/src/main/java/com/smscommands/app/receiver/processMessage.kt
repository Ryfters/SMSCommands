package com.smscommands.app.receiver

import android.content.Context
import android.telephony.SmsManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.smscommands.app.R
import com.smscommands.app.commands.Command
import com.smscommands.app.data.SyncPreferences
import com.smscommands.app.data.db.HistoryDatabase
import com.smscommands.app.data.db.HistoryRepository
import com.smscommands.app.receiver.CommandStatus.DISABLED_COMMAND
import com.smscommands.app.receiver.CommandStatus.INVALID_COMMAND
import com.smscommands.app.receiver.CommandStatus.INVALID_PIN
import com.smscommands.app.receiver.CommandStatus.MISSING_PERMISSIONS
import com.smscommands.app.receiver.CommandStatus.SUCCESS
import com.smscommands.app.ui.navigation.dataStore
import java.time.Instant

fun processMessage(context: Context, sender: String, message: String) {
    if (message.startsWith("!!") == false) return

    val dataStore: DataStore<Preferences> = context.dataStore
    val database = HistoryRepository(HistoryDatabase.getDatabase(context).historyDao())
    val syncPreferences = SyncPreferences(dataStore, database)

    val splitMessage = message.split("/")
    val inputtedPin = splitMessage[0].removePrefix("!!")
    val inputtedCommand = splitMessage.getOrElse(1) { "" }
    val inputtedParams = splitMessage.getOrElse(2) { "" }.split(" ")

    val onReply: (String) -> Unit = { message ->
        val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
        smsManager.sendTextMessage(sender, null, message, null, null)
    }

    val pinCorrect = inputtedPin == syncPreferences.readPin()

    val selectedCommand: Command? = Command.LIST.find { command ->
        val commandLabel = context.getString(command.label)
        commandLabel.lowercase() == inputtedCommand.lowercase()
    }

    var status = ""
    var commandId: String = selectedCommand?.id ?: INVALID_COMMAND

    var commandToRun: Command? = null

    val missingPermissions = selectedCommand?.let { command ->
        command.requiredPermissions.filterNot { permission ->
            permission.isGranted(context)
        }
    }

    if (pinCorrect == false) {
        onReply(context.getString(R.string.sms_reply_pin_invalid))
        status = INVALID_PIN
    } else if (selectedCommand == null) {
        onReply(context.getString(R.string.sms_reply_command_invalid))
        status = INVALID_COMMAND
    } else if (syncPreferences.readCommandEnabled(selectedCommand.id) == false) {
        onReply(context.getString(R.string.sms_reply_command_disabled))
        status = DISABLED_COMMAND
    } else if (missingPermissions!!.isNotEmpty()) { // Null asserted in l56
        val replyContent = missingPermissions.joinToString { permission ->
            context.getString(permission.label)
        }
        onReply(context.getString(R.string.sms_reply_missing_permissions, replyContent))
        status = MISSING_PERMISSIONS
    } else {
        commandToRun = selectedCommand // Save the command to run after being saved to history
        status = SUCCESS
    }

    if (syncPreferences.readHistoryEnabled()) {
        syncPreferences.saveHistoryItem(
            time = Instant.now(),
            commandId = commandId,
            status = status,
            sender = sender,
            message = message
        )
    }

    commandToRun?.onReceive(context, inputtedParams, sender, onReply)

}

object CommandStatus {
    const val SUCCESS = "success"
    const val INVALID_PIN = "invalid_pin"
    const val INVALID_COMMAND = "invalid_command"
    const val DISABLED_COMMAND = "disabled_command"
    const val MISSING_PERMISSIONS = "missing_permissions"

}