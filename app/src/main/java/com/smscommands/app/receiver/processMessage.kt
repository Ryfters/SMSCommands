package com.smscommands.app.receiver

import android.content.Context
import android.telephony.SmsManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.smscommands.app.R
import com.smscommands.app.commands.Command
import com.smscommands.app.commands.params.FlagParamDefinition
import com.smscommands.app.commands.params.OptionParamDefinition
import com.smscommands.app.data.SyncPreferences
import com.smscommands.app.data.db.HistoryDatabase
import com.smscommands.app.data.db.HistoryRepository
import com.smscommands.app.receiver.CommandStatus.DISABLED_COMMAND
import com.smscommands.app.receiver.CommandStatus.INVALID_ARGS
import com.smscommands.app.receiver.CommandStatus.INVALID_COMMAND
import com.smscommands.app.receiver.CommandStatus.INVALID_PARAM
import com.smscommands.app.receiver.CommandStatus.INVALID_PIN
import com.smscommands.app.receiver.CommandStatus.MISSING_PARAM
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
    val inputtedParams = splitMessage.getOrElse(2) { "" }
            .split(" ")
            .filterNot { it == "" }
            .map { it.lowercase() }
            .toMutableList()

    val onReply: (String) -> Unit = { message ->
        val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
        smsManager.sendTextMessage(sender, null, message, null, null)
    }

    val pinCorrect = inputtedPin == syncPreferences.readPin()

    val selectedCommand: Command? = Command.LIST.find { command ->
        val commandLabel = context.getString(command.label)
        commandLabel.lowercase() == inputtedCommand.lowercase()
    }

    var status = SUCCESS
    var commandId: String = selectedCommand?.id ?: INVALID_COMMAND
    val commandParams = mutableMapOf<String, Any>()

    if (pinCorrect == false) {
        onReply(context.getString(R.string.sms_reply_pin_invalid))
        status = INVALID_PIN
    } else if (selectedCommand == null) {
        onReply(context.getString(R.string.sms_reply_command_invalid))
        status = INVALID_COMMAND
    } else if (syncPreferences.readCommandEnabled(selectedCommand.id) == false) {
        onReply(context.getString(R.string.sms_reply_command_disabled))
        status = DISABLED_COMMAND
    } else if (
        selectedCommand.requiredPermissions.filterNot { it.isGranted(context) }.isNotEmpty()
    ) {
        val missingPermissions = selectedCommand.requiredPermissions.filterNot { it.isGranted(context) }
            val replyContent = missingPermissions.joinToString { permission ->
                context.getString(permission.label)
            }
            onReply(context.getString(R.string.sms_reply_missing_permissions, replyContent))
            status = MISSING_PERMISSIONS
    } else {
        for (param in selectedCommand.params) {
            val paramName = context.getString(param.value.name).lowercase()
            if (param.value is FlagParamDefinition) {
                val paramValue = inputtedParams.contains(paramName)
                if (paramValue) inputtedParams.remove(paramName)
                commandParams[param.key] = paramValue
                continue
            }

            val optionParam = param.value as OptionParamDefinition

            val inputtedParamIndex = inputtedParams.indexOfFirst { it.startsWith("$paramName:") }

            if (inputtedParamIndex == -1) {
                if (optionParam.required) {
                    onReply(context.getString(R.string.sms_reply_missing_param, paramName))
                    status = MISSING_PARAM
                    break
                }
                commandParams[param.key] = optionParam.defaultValue
                    ?: throw IllegalStateException("Missing default value for $paramName")
                continue
            }


            val inputtedArgument = inputtedParams[inputtedParamIndex].removePrefix("$paramName:")

            if (!optionParam.verifyParam(context, inputtedArgument)) {
                onReply(context.getString(
                    R.string.sms_reply_invalid_args,
                    paramName,
                    optionParam.possibleValues(context)
                ))
                status = INVALID_ARGS
                break
            }

            commandParams[param.key] = optionParam.parseParam(context, inputtedArgument)

            inputtedParams.removeAt(inputtedParamIndex)
        }
        if (inputtedParams.isNotEmpty()) {
            status = INVALID_PARAM
            onReply(context.getString(
                R.string.sms_reply_invalid_params,
                inputtedParams.joinToString { "'$it'" }
            ))
        }
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

    if (status == SUCCESS) {
        selectedCommand?.onReceive(context, commandParams, sender, onReply)
    }
}

object CommandStatus {
    const val DISABLED_COMMAND = "disabled_command"
    const val INVALID_ARGS = "invalid_param_value"
    const val INVALID_COMMAND = "invalid_command"
    const val INVALID_PARAM = "invalid_param"
    const val INVALID_PIN = "invalid_pin"
    const val MISSING_PARAM = "missing_param"
    const val MISSING_PERMISSIONS = "missing_permissions"
    const val SUCCESS = "success"

}