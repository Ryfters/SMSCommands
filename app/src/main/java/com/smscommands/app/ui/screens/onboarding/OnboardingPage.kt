package com.smscommands.app.ui.screens.onboarding

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

interface OnboardingPage {
    @get:StringRes
    val title: Int

    @Composable
    fun Content()

    companion object {
        val LIST = listOf(
            WelcomePage(),
            CommandsPage(),
            ParametersPage(),
        )
    }
}
