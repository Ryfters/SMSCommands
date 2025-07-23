package com.ryfter.smscommands.ui.screens.commands

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ryfter.smscommands.R
import com.ryfter.smscommands.commands.Command
import com.ryfter.smscommands.commands.params.FlagParamDefinition
import com.ryfter.smscommands.commands.params.OptionParamDefinition
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.permissions.Permission
import com.ryfter.smscommands.ui.components.MainScaffold
import com.ryfter.smscommands.ui.components.MyListItem
import com.ryfter.smscommands.ui.components.Subtitle
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.navigation.navigate
import com.ryfter.smscommands.ui.navigation.pop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandItemScreen(
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel,
    commandId: String
) {
    val command = Command.LIST.find { it.id == commandId } ?: run {
        Log.e("CommandItemDialog", "Command not found: $commandId")
        backStack.pop()
        return
    }

    MainScaffold(
        backStack = backStack,
        title = stringResource(command.label),
        subtitle = stringResource(command.description),
        showUpButton = true,
    ) {
        val context = LocalContext.current

        val permissionsState by viewModel.permissionsState.collectAsState()

        val missingPermissions = (command.requiredPermissions + Permission.BASE)
            .filter { permission -> permissionsState[permission.id] == false }

        val isMissingPerms = missingPermissions.isNotEmpty()

        val isEnabled = viewModel.commandPreferences.collectAsState().value[commandId] == true

        val status =
            if (isMissingPerms) stringResource(R.string.screen_commands_status_missing_perms)
            else if (isEnabled) stringResource(R.string.common_enabled)
            else stringResource(R.string.common_disabled)

        val requiredPermissions =
            command.requiredPermissions.joinToString { context.getString(it.label) }
                .takeIf { it.isNotEmpty() } ?: stringResource(R.string.common_none)

        val flags = command.params.filter { it.value is FlagParamDefinition }

        @Suppress("UNCHECKED_CAST")
        val options = command.params.filter { it.value is OptionParamDefinition }
            as Map<String, OptionParamDefinition>

        Subtitle(stringResource(R.string.common_details))
        MyListItem(
            title = stringResource(R.string.screen_commands_status),
            content = status,
            onClick = if (isMissingPerms) {
                { backStack.navigate(Route.Perms.PMain) }
            } else null
        )
        MyListItem(
            title = stringResource(R.string.screen_commands_permissions_required),
            content = requiredPermissions,
        )

        Subtitle(stringResource(R.string.screen_commands_flags))
        if (flags.isEmpty()) {
            MyListItem(stringResource(R.string.common_none))
        } else {
            flags.values.forEach { param ->
                MyListItem(
                    title = stringResource(param.name),
                    content = stringResource(param.desc)
                )
            }
        }

        Subtitle(stringResource(R.string.screen_commands_options))
        if (options.isEmpty()) {
            MyListItem(stringResource(R.string.common_none))
        } else {
            options.values.forEach { option ->
                val content =
                    stringResource(
                        R.string.screen_commands_options_content,
                        stringResource(option.desc),
                        option.possibleValues(LocalContext.current)
                    )

                MyListItem(
                    title = stringResource(option.name),
                    content = content,
                    maxContentLines = Int.MAX_VALUE,
                )
            }
        }

        command.extraContent?.let {
            Subtitle("Other")
            it(backStack, viewModel)
        }

        Spacer(
            Modifier.padding(
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
        )
    }
}