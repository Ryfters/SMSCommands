package com.smscommands.app.ui.screens.onboarding

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.smscommands.app.R

class WelcomePage : OnboardingPage {
    override val title = R.string.screen_onboarding_intro_title

    @Composable
    override fun Content() {
        Text(
            stringResource(R.string.screen_onboarding_intro_content)
        )
    }

}