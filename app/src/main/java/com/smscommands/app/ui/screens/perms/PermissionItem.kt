package com.smscommands.app.ui.screens.perms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.smscommands.app.R
import com.smscommands.app.commands.Command
import com.smscommands.app.permissions.Permission
import com.smscommands.app.ui.components.MyListItem

@Composable
fun PermissionItem(
    permission: Permission,
) {
    val context = LocalContext.current

    val content = if (permission.required || permission.optional) {
        permission.description?.let { stringResource(it) } ?: ""
    } else {
        val permRequiredFor = Command.LIST.filter { command ->
            command.requiredPermissions.any { it.id == permission.id }
        }.joinToString { context.getString(it.label) }

        stringResource(R.string.screen_perms_perm_required_for, permRequiredFor)
    }

    // TODO: Use this in viewmodel
    var isGranted by remember { mutableStateOf(permission.isGranted(context)) }
    val isGrantedString =
        if (isGranted) stringResource(R.string.screen_perms_granted)
        else stringResource(R.string.screen_perms_not_granted)

    val request = permission.request { isGranted = it }



    MyListItem(
        title = stringResource(permission.label),
        content = stringResource(R.string.common_v1_comma_v2, isGrantedString, content),
        onClick = request
    )
}