package com.ryfter.smscommands.ui.screens.onboarding.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ryfter.smscommands.R
import com.ryfter.smscommands.commands.Command
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.permissions.Permission
import com.ryfter.smscommands.ui.components.MyListItem
import com.ryfter.smscommands.ui.navigation.Routes
import com.ryfter.smscommands.ui.screens.onboarding.OnboardingPage

class CommandsPage : OnboardingPage {
    override val title = R.string.screen_onboarding_commands_title

    private var isAnyCommandEnabled by mutableStateOf(false)

    @Composable
    override fun Content(
        viewModel: UiStateViewModel,
        navController: NavController,
        modifier: Modifier
    ) {
        val context = LocalContext.current

        val commandPreferences by viewModel.commandPreferences.collectAsState()
        val permissionsState by viewModel.permissionsState.collectAsState()

        isAnyCommandEnabled = commandPreferences.any { it.value }

        Column(modifier) {
            Text(
                stringResource(R.string.screen_onboarding_commands_info),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )

            HorizontalDivider(Modifier.padding(horizontal = 24.dp))

            Command.LIST.forEach { command ->
                val missingPermissions = (command.requiredPermissions + Permission.BASE)
                    .filter { permission -> permissionsState[permission.id] == false }

                val disabled = missingPermissions.isNotEmpty()

                val content = if (disabled) stringResource(
                    R.string.screen_commands_missing_permissions,
                    missingPermissions.joinToString { context.getString(it.label) }
                ) else stringResource(command.description)


                MyListItem(
                    title = stringResource(command.label),
                    content = content,
                    onClick = { navController.navigate(Routes.Commands.ITEM + command.id) },
                    separator = true,
                    action = {
                        Switch(
                            checked = commandPreferences[command.id] == true,
                            onCheckedChange = { value ->
                                viewModel.updateCommandPreference(command.id, value)
                            },
                            enabled = !disabled
                        )
                    }
                )
            }
        }
    }

    override val nextButtonText: Int
        get() = if (isAnyCommandEnabled) R.string.common_continue else R.string.common_skip
}