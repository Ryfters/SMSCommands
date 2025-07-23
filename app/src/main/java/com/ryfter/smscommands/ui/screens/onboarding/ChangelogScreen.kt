package com.ryfter.smscommands.ui.screens.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.components.MainScaffold
import com.ryfter.smscommands.ui.navigation.MyNavBackStack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangelogScreen(
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel,
) {
    MainScaffold(
        backStack = backStack,
        title = stringResource(R.string.screen_onboarding_changelog_title),
    ) {
        Column(Modifier.padding(16.dp)) {
            Changelog.values.forEach { change ->
                Row(Modifier.fillMaxWidth()) {
                    Text(change.versionName, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.weight(1f))
                    Text(change.date, style = MaterialTheme.typography.headlineSmall)
                }
                HorizontalDivider(Modifier
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 4.dp))
                change.changes.forEach {
                    Text(" - $it")
                }
                Spacer(Modifier.padding(8.dp))
            }
        }
    }
}