package com.ryfter.smscommands.ui.screens.perms

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.permissions.Permission
import com.ryfter.smscommands.ui.components.MainScaffold
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.navigation.navigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermsScreen(
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel,
    permissionIds: List<String> = emptyList()
) {
    MainScaffold(
        backStack = backStack,
        title = stringResource(R.string.screen_perms_title),
        showUpButton = true,
        actions = {
            IconButton(
                onClick = { backStack.navigate(Route.Perms.DeclineWarningDialog) }
            ) {
                Icon(painterResource(R.drawable.ic_warning), contentDescription = null)
            }
        }
    ) {
        val permissionsState by viewModel.permissionsState.collectAsState()

        Permission.ALL.forEach { permission ->
            PermissionItem(
                permission = permission,
                isGranted = permissionsState[permission.id] == true,
                highlight = permissionIds.contains(permission.id) && permissionsState[permission.id] != true,
                onGrant = { isEnabled ->
                    viewModel.updateSinglePermissionState(permission.id, isEnabled)
                    if (!isEnabled) backStack.navigate(Route.Perms.DeclineWarningDialog)
                }
            )
        }
    }
}
