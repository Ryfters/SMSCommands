package com.smscommands.app.ui.screens.onboarding

import androidx.annotation.StringRes
import com.smscommands.app.R

val OnboardingPages = listOf(
    OnboardingPage(
        title = R.string.onboarding_intro_title,
        content = R.string.screen_onboarding_intro_description,
    ),
    OnboardingPage(
        title = R.string.screen_home_title,
        content = R.string.screen_onboarding_intro_description,
    ),
    OnboardingPage(
        title = R.string.app_name,
        content = R.string.common_stop,
    ),
    OnboardingPage(
        title = R.string.onboarding_intro_title,
        content = R.string.screen_onboarding_intro_description,
    ),
)

data class OnboardingPage(@get:StringRes val title: Int, @get:StringRes val content: Int)