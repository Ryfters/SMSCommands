package com.smscommands.app.commands

import android.content.Context
import com.smscommands.app.R
import com.smscommands.app.commands.params.CustomOptionParamDefinition
import com.smscommands.app.commands.params.FlagParamDefinition
import com.smscommands.app.commands.params.OptionParamDefinition
import com.smscommands.app.permissions.Permission
import com.smscommands.app.utils.joinToStringOrNone
import com.smscommands.app.utils.reply

class Help : Command {
    override val id = "command_help"

    override val label = R.string.command_help_label
    override val description = R.string.command_help_desc

    override val requiredPermissions = listOf<Permission>()

    override val params = mapOf(
        COMMAND_PARAM to CustomOptionParamDefinition(
            name = R.string.command_help_param_command,
            desc = R.string.command_help_param_command_desc,
            _verifyParam = verifyCommandParam,
            _parseParam = parseCommandParam,
            _possibleValues = commandParamValues
        )
    )

    override fun onReceive(
        context: Context,
        parameters: Map<String, Any?>,
        sender: String,
        id: Long?
    ) {
        val command = Command.LIST.find { it.id == parameters[COMMAND_PARAM] as String? }

        if (command != null) {
            return reply(context, getCommandHelpMessage(context, command), sender, id)
        }

        val commandList = Command.LIST.joinToString(context.getString(R.string.common_separator)) {
            context.getString(it.label)
        }
        reply(
            context,
            context.getString(R.string.command_help_reply, commandList), sender, id
        )
    }

    companion object {
        const val COMMAND_PARAM = "command_param"
    }
}


private val verifyCommandParam: (Context, String) -> Boolean = { context, param ->
    Command.LIST.any { context.getString(it.label).lowercase() == param.lowercase() }
}
private val parseCommandParam: (Context, String) -> Any = { context, param ->
    Command.LIST.first { context.getString(it.label).lowercase() == param.lowercase() }.id
}
private val commandParamValues: (Context) -> String = { context ->
    Command.LIST.joinToString { context.getString(it.label) }
}


private fun getCommandHelpMessage(context: Context, command: Command): String {
    val label = context.getString(command.label)
    val desc = context.getString(command.description)

    val permsString = command.requiredPermissions.joinToStringOrNone(
        "  " + context.getString(R.string.common_none),
        context.getString(R.string.common_separator)
    ) {
        "    ${context.getString(it.label)}"
    }

    val params = command.params.filter { it.value is OptionParamDefinition }.values
        .joinToStringOrNone("  " + context.getString(R.string.common_none), "\n") {
            "    ${context.getString(it.name)}: ${context.getString(it.desc)}"
        }

    val flags = command.params.filter { it.value is FlagParamDefinition }.values
        .joinToStringOrNone("  " + context.getString(R.string.common_none), "\n") {
            "    ${context.getString(it.name)}: ${context.getString(it.desc)}"
        }

    return context.getString(
        R.string.command_help_reply_command,
        label, desc, permsString, params, flags
    )
}