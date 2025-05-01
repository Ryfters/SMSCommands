package com.smscommands.app.ui.screens.onboarding

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

val OnboardingPages = listOf(
    WelcomePage(),
    CommandsPage(),
    ParametersPage(),
)

interface OnboardingPage {
    @get:StringRes
    val title: Int

    @Composable
    fun Content()
}
