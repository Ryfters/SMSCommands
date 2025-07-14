package com.ryfter.smscommands.ui.screens.perms

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.permissions.Permission
import com.ryfter.smscommands.ui.components.MainScaffold
import com.ryfter.smscommands.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermsScreen(
    navController: NavController,
    viewModel: UiStateViewModel,
) {
    MainScaffold(
        navController = navController,
        title = stringResource(R.string.screen_perms_title),
        showUpButton = true,
        actions = {
            IconButton(
                onClick = { navController.navigate(Routes.Perms.DECLINE_WARNING_DIALOG)}
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
                onGrant = { isEnabled ->
                    viewModel.updateSinglePermissionState(permission.id, isEnabled)
                    if (isEnabled == false)
                        navController.navigate(Routes.Perms.DECLINE_WARNING_DIALOG)
                }
            )
        }
    }
}
