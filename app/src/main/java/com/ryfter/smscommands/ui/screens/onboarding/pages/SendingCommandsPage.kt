package com.ryfter.smscommands.ui.screens.onboarding.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.components.BoldedKeyValue
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.screens.onboarding.OnboardingPage

class SendingCommandsPage : OnboardingPage {
    override val title = R.string.screen_onboarding_command_title

    @Composable
    override fun Content(
        viewModel: UiStateViewModel,
        backStack: MyNavBackStack,
        modifier: Modifier
    ) {
        Column(modifier.padding(horizontal = 16.dp)) {
            Text(
                text = stringResource(R.string.screen_onboarding_sending_info),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = stringResource(R.string.screen_onboarding_sending_prefix)
                        + stringResource(R.string.screen_onboarding_sending_pin) + " "
                        + stringResource(R.string.screen_onboarding_sending_command) + " "
                        + stringResource(R.string.screen_onboarding_sending_params),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            HorizontalDivider(Modifier.padding(horizontal = 8.dp))

            BoldedKeyValue(
                key = stringResource(R.string.screen_onboarding_sending_prefix),
                value = stringResource(R.string.screen_onboarding_sending_prefix_value),
                modifier = Modifier.padding(top = 8.dp)
            )

            BoldedKeyValue(
                key = stringResource(R.string.screen_onboarding_sending_pin),
                value = stringResource(R.string.screen_onboarding_sending_pin_value),
                modifier = Modifier.padding(top = 8.dp)
            )

            BoldedKeyValue(
                key = stringResource(R.string.screen_onboarding_sending_command),
                value = stringResource(R.string.screen_onboarding_sending_command_value),
                modifier = Modifier.padding(top = 8.dp)
            )

            BoldedKeyValue(
                key = stringResource(R.string.screen_onboarding_sending_params),
                value = stringResource(R.string.screen_onboarding_sending_params_value),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}