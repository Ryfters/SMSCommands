package com.ryfter.smscommands.ui.screens.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ryfter.smscommands.BuildConfig
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.navigation.pop
import com.ryfter.smscommands.ui.navigation.replace

@Composable
fun UpdateDialog(
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel,
) {
    val versionCode = BuildConfig.VERSION_CODE

    AlertDialog(
        onDismissRequest = {
            backStack.pop()
            viewModel.updateLastBuildCode(versionCode)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    backStack.pop()
                    viewModel.updateLastBuildCode(versionCode)
                }
            ) {
                Text(stringResource(R.string.common_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    backStack.replace(Route.Onboarding.ChangelogScreen)
                    viewModel.updateLastBuildCode(versionCode)
                }
            ) {
                Text(stringResource(R.string.screen_onboarding_changelog_dialog_goto_changelog))
            }
        },
        title = {
            Text(
                stringResource(
                    R.string.screen_onboarding_changelog_dialog_app_updated,
                    BuildConfig.VERSION_NAME
                )
            )
        },
        text = {
            Column {
                Changelog[versionCode]?.let { it ->
                    Text(
                        stringResource(R.string.screen_onboarding_changelog_dialog_highlights),
                        style = MaterialTheme.typography.titleLarge
                    )
                    it.changes.forEach { change ->
                        Text(
                            stringResource(R.string.common_bulletpoint, change)
                        )
                    }
                } ?: Text(stringResource(R.string.screen_onboarding_changelog_dialog_nochanges))
            }
        },
    )
}