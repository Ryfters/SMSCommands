package com.ryfter.smscommands.ui.screens.onboarding.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.screens.onboarding.OnboardingPage

class ExamplesPage : OnboardingPage {
    override val title = R.string.screen_onboarding_examples_title

    @Composable
    override fun Content(
        viewModel: UiStateViewModel,
        backStack: MyNavBackStack,
        modifier: Modifier
    ) {
        val boldStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)

        Column(modifier.padding(horizontal = 16.dp)) {

            Text(stringResource(R.string.screen_onboarding_examples_1_cmd), style = boldStyle)
            Text(stringResource(R.string.screen_onboarding_examples_1_desc))

            Text(
                stringResource(R.string.screen_onboarding_examples_2_cmd),
                Modifier.padding(top = 8.dp),
                style = boldStyle
            )
            Text(stringResource(R.string.screen_onboarding_examples_2_desc))

            Text(
                stringResource(R.string.screen_onboarding_examples_3_cmd),
                Modifier.padding(top = 8.dp),
                style = boldStyle
            )
            Text(stringResource(R.string.screen_onboarding_examples_3_desc))
        }
    }
}