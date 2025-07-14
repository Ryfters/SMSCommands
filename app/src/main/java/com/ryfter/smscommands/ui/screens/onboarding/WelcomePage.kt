package com.ryfter.smscommands.ui.screens.onboarding

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ryfter.smscommands.R

class WelcomePage : OnboardingPage {
    override val title = R.string.screen_onboarding_intro_title

    @Composable
    override fun Content() {
        Text(
            stringResource(R.string.screen_onboarding_intro_content)
        )
    }

}