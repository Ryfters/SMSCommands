package com.ryfter.smscommands.ui.screens.perms

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ryfter.smscommands.R
import com.ryfter.smscommands.commands.Command
import com.ryfter.smscommands.permissions.Permission
import com.ryfter.smscommands.ui.components.MyListItem

@Composable
fun PermissionItem(
    permission: Permission,
    isGranted: Boolean,
    onGrant: (Boolean) -> Unit,
) {
    val context = LocalContext.current

    val content =
        if (!Permission.COMMANDS.contains(permission))
            permission.description?.let { stringResource(it) } ?: ""
        else
            Command.LIST.filter { it.requiredPermissions.contains(permission) }
                .joinToString { context.getString(it.label) }
                .let { stringResource(R.string.screen_perms_perm_required_for, it) }


    val isGrantedString =
        if (isGranted) stringResource(R.string.screen_perms_granted)
        else stringResource(R.string.screen_perms_not_granted)

    val request =
        if (!isGranted) permission.request(onResult = onGrant)
        else null

    MyListItem(
        title = stringResource(permission.label),
        content = isGrantedString + stringResource(R.string.common_separator) + content,
        onClick = request
    )
}