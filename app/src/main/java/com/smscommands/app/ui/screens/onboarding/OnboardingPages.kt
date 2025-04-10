package com.smscommands.app.ui.screens.onboarding

import androidx.annotation.StringRes
import com.smscommands.app.R

val OnboardingPages = listOf(
    OnboardingPage(
        title = R.string.screen_onboarding_intro_title,
        content = R.string.screen_onboarding_intro_content,
    ),
    OnboardingPage(
        title = R.string.screen_onboarding_command_title,
        content = R.string.screen_onboarding_command_content,
    ),
    OnboardingPage(
        title = R.string.screen_onboarding_parameters_title,
        content = R.string.screen_onboarding_parameters_content,
    ),
)

data class OnboardingPage(@get:StringRes val title: Int, @get:StringRes val content: Int)