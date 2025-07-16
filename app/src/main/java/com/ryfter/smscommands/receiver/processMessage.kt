package com.ryfter.smscommands.receiver

import android.content.Context
import android.telephony.SmsManager
import com.ryfter.smscommands.R
import com.ryfter.smscommands.commands.Command
import com.ryfter.smscommands.commands.params.FlagParamDefinition
import com.ryfter.smscommands.commands.params.OptionParamDefinition
import com.ryfter.smscommands.data.SyncPreferences
import com.ryfter.smscommands.receiver.CommandStatus.INVALID_COMMAND
import java.time.Instant

fun processMessage(context: Context, sender: String, trigger: String) {
    if (!trigger.startsWith("!!")) return

    val syncPreferences = SyncPreferences.getPreferences(context)

    val splitTrigger = trigger.split("/")
    val inputtedPin = splitTrigger[0].removePrefix("!!")
    val inputtedCommand = splitTrigger.getOrElse(1) { "" }
    val inputtedParams = splitTrigger.getOrElse(2) { "" }
            .split(" ")
            .filterNot { it == "" }
            .map { it.lowercase() }
            .toMutableList()

    val pinCorrect = inputtedPin == syncPreferences.readPin()

    val selectedCommand: Command? = Command.LIST.find { command ->
        val commandLabel = context.getString(command.label)
        commandLabel.lowercase() == inputtedCommand.lowercase()
    }

    val messagesToSend = mutableListOf<String>()
    var status: Int = R.string.status_success
    val commandId: String = selectedCommand?.id ?: INVALID_COMMAND
    val commandParams = mutableMapOf<String, Any?>()

    if (!pinCorrect) {
        messagesToSend.add(context.getString(R.string.sms_reply_pin_invalid))
        status = R.string.status_invalid_pin
    } else if (selectedCommand == null) {
        messagesToSend.add(context.getString(R.string.sms_reply_command_invalid))
        status = R.string.status_invalid_command
    } else if (!syncPreferences.readCommandEnabled(selectedCommand.id)) {
        messagesToSend.add(context.getString(R.string.sms_reply_command_disabled))
        status = R.string.status_disabled_command
    } else if (
        selectedCommand.requiredPermissions.filterNot { it.isGranted(context) }.isNotEmpty()
    ) {
        val missingPermissions = selectedCommand.requiredPermissions.filterNot { it.isGranted(context) }
            val replyContent = missingPermissions.joinToString { permission ->
                context.getString(permission.label)
            }
            messagesToSend.add(context.getString(R.string.sms_reply_missing_permissions, replyContent))
        status = R.string.status_missing_permissions
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
                    messagesToSend.add(context.getString(R.string.sms_reply_missing_param, paramName))
                    status = R.string.status_missing_param
                    break
                }
                commandParams[param.key] = optionParam.defaultValue
                continue
            }


            val inputtedArgument = inputtedParams[inputtedParamIndex].removePrefix("$paramName:")

            if (!optionParam.verifyParam(context, inputtedArgument)) {
                messagesToSend.add(context.getString(
                    R.string.sms_reply_invalid_args,
                    paramName,
                    optionParam.possibleValues(context)
                ))
                status = R.string.status_invalid_args
                break
            }

            commandParams[param.key] = optionParam.parseParam(context, inputtedArgument)

            inputtedParams.removeAt(inputtedParamIndex)
        }
        if (
            inputtedParams.isNotEmpty() &&
            status == R.string.status_success  // Don't trigger this if an error already occurred
        ) {
            status = R.string.status_invalid_param
            messagesToSend.add(context.getString(
                R.string.sms_reply_invalid_params,
                inputtedParams.joinToString { "'$it'" }
            ))
        }
    }

    val historyId = if (syncPreferences.readHistoryEnabled()) {
        syncPreferences.saveHistoryItem(
            time = Instant.now(),
            commandId = commandId,
            status = status,
            sender = sender,
            trigger = trigger,
            messages = messagesToSend
        )
    } else null

    val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
    messagesToSend.forEach { smsManager.sendTextMessage(sender, null, it, null, null) }

    if (status == R.string.status_success)
        selectedCommand?.onReceive(context, commandParams, sender, historyId)
}

object CommandStatus {
    const val INVALID_COMMAND = "invalid_command"
}