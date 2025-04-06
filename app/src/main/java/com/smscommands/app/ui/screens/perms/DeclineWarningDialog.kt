package com.smscommands.app.ui.screens.perms

import android.content.Intent
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.smscommands.app.R

@Composable
fun DeclineWarningDialog(
    navController: NavController
) {
    val context = LocalContext.current


    AlertDialog(
        title = { Text(stringResource(R.string.common_warning)) },

        // TODO: extract res
        text = { Text(stringResource(R.string.screen_perms_decline_warning)) },
        confirmButton = {
            TextButton(
                onClick = { navController.popBackStack() }
            ) {
                Text(stringResource(R.string.common_ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = "package:${context.packageName}".toUri()
                    }
                    context.startActivity(intent)
                    navController.popBackStack()
                }
            ) {
                Text("Go to settings")
            }
        },
        onDismissRequest = { navController.popBackStack() },
        icon = {
            Icon(painterResource(R.drawable.ic_warning), contentDescription = null)
        }
    )
}