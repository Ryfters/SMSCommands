package com.ryfter.smscommands.ui.screens.onboarding.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.components.BoldedKeyValue
import com.ryfter.smscommands.ui.screens.onboarding.OnboardingPage

class ParametersPage : OnboardingPage {
    override val title = R.string.screen_onboarding_parameters_title

    @Composable
    override fun Content(
        viewModel: UiStateViewModel,
        navController: NavController,
        modifier: Modifier
    ) {
        Column(
            modifier.padding(horizontal = 16.dp)
        ) {
            Text(stringResource(R.string.screen_onboarding_parameters_info))

            BoldedKeyValue(
                key = stringResource(R.string.screen_onboarding_parameters_flags_key),
                value = stringResource(R.string.screen_onboarding_parameters_flags_value),
                modifier = Modifier.padding(top = 8.dp)
            )

            BoldedKeyValue(
                key = stringResource(R.string.screen_onboarding_parameters_options_key),
                value = stringResource(R.string.screen_onboarding_parameters_options_value),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}