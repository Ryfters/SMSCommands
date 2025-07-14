package com.ryfter.smscommands.ui.screens.commands

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ryfter.smscommands.R
import com.ryfter.smscommands.commands.Command
import com.ryfter.smscommands.commands.params.FlagParamDefinition
import com.ryfter.smscommands.commands.params.OptionParamDefinition
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.permissions.Permission
import com.ryfter.smscommands.ui.components.MainScaffold
import com.ryfter.smscommands.ui.components.MyIconButton
import com.ryfter.smscommands.ui.components.MyListItem
import com.ryfter.smscommands.ui.components.Subtitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandItemScreen(
    navController: NavController,
    viewModel: UiStateViewModel,
    commandId: String
) {
    val command = Command.LIST.find { it.id == commandId } ?: run {
        Log.e("CommandItemDialog", "Command not found: $commandId")
        navController.popBackStack()
        return
    }

    MainScaffold(
        navController = navController,
        title = stringResource(command.label),
        subtitle = stringResource(command.description),
        showUpButton = true,
    ) {
        val context = LocalContext.current

        val permissionsState by viewModel.permissionsState.collectAsState()

        val isMissingPerms = (command.requiredPermissions + Permission.BASE).any { permission ->
            permissionsState[permission.id] == true
        }

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
        val params = command.params.filter { it.value is OptionParamDefinition }
            as Map<String, OptionParamDefinition>

        Subtitle(stringResource(R.string.common_details))
        MyListItem(
            title = stringResource(R.string.screen_commands_status),
            content = status,
        )
        MyListItem(
            title = stringResource(R.string.screen_commands_permissions_required),
            content = requiredPermissions,
        )

        Subtitle("Flags")
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

        Subtitle("Parameters")
        if (params.isEmpty()) {
            MyListItem(stringResource(R.string.common_none))
        } else {
            params.values.forEach { param ->
                var expandable by remember { mutableStateOf(false) }
                var expanded by remember { mutableStateOf(false) }

                val maxContentLines by animateIntAsState(if (expanded) Int.MAX_VALUE else 2)


                val targetRotation = if (expanded) -90f else 0f
                val animatedRotation by animateFloatAsState(targetRotation)

                MyListItem(
                    title = stringResource(param.name),
                    content = stringResource(param.desc),
                    action = {
                        if (expandable || expanded) {
                            MyIconButton(
                                icon = painterResource(R.drawable.ic_expand),
                                onClick = { expanded = !expanded },
                                modifier = Modifier.rotate(animatedRotation)
                            )
                        }
                    },
                    maxContentLines = if (maxContentLines > 1) maxContentLines else 1,
                    onContentLayout = { result ->
                        expandable = result.hasVisualOverflow
                    }
                )
            }
        }
    }
}