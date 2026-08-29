package com.ryfter.smscommands.ui.screens.perms

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.components.MyTextButton
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.pop

@Suppress("unused")
@Composable
fun ShizukuNotFoundDialog(
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel
) {
    val uriHandler = LocalUriHandler.current
    val installGuideUrl = stringResource(R.string.url_shizuku_install_guide)

    AlertDialog(
        title = { Text(stringResource(R.string.screen_perms_shizuku_missing)) },

        text = { Text(stringResource(R.string.screen_perms_shizuku_missing_desc)) },
        confirmButton = {
            MyTextButton(stringResource(R.string.common_ok)) { backStack.pop() }
        },
        dismissButton = {
            MyTextButton(stringResource(R.string.screen_perms_shizuku_install_guide)) {
                uriHandler.openUri(installGuideUrl)
                backStack.pop()
            }
        },
        onDismissRequest = {},
        icon = {
            Icon(painterResource(R.drawable.ic_warning), contentDescription = null)
        }
    )
}