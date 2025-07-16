package com.ryfter.smscommands.ui.screens.onboarding.pages

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.screens.onboarding.OnboardingPage

class WelcomePage : OnboardingPage {
    override val title = R.string.screen_onboarding_intro_title

    @Composable
    override fun Content(
        viewModel: UiStateViewModel,
        navController: NavController,
        modifier: Modifier
    ) {
        Text(
            text = stringResource(R.string.screen_onboarding_intro_content),
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }

    override val nextButtonText: Int
        get() = R.string.common_start

}