package com.smscommands.app.ui.screens.perms

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.smscommands.app.R
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.permissions.Permission
import com.smscommands.app.ui.components.MainScaffold
import com.smscommands.app.ui.navigation.Routes

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
                onClick = { navController.navigate(Routes.DECLINE_WARNING_DIALOG)}
            ) {
                Icon(painterResource(R.drawable.ic_warning), contentDescription = null)
            }
        }
    ) {

        LazyColumn {
            items(Permission.LIST) { permission ->
                PermissionItem(permission)
            }
        }
    }
}
