package com.ryfter.smscommands.ui.screens.onboarding

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.screens.onboarding.pages.CommandsPage
import com.ryfter.smscommands.ui.screens.onboarding.pages.ExamplesPage
import com.ryfter.smscommands.ui.screens.onboarding.pages.FinishPage
import com.ryfter.smscommands.ui.screens.onboarding.pages.ParametersPage
import com.ryfter.smscommands.ui.screens.onboarding.pages.PermsPage
import com.ryfter.smscommands.ui.screens.onboarding.pages.PinPage
import com.ryfter.smscommands.ui.screens.onboarding.pages.SendingCommandsPage
import com.ryfter.smscommands.ui.screens.onboarding.pages.WelcomePage

interface OnboardingPage {
    @get:StringRes
    val title: Int

    @Composable
    fun Content(viewModel: UiStateViewModel, navController: NavController, modifier: Modifier)

    /**
     * Return `true` if the user should be navigated to the next page, `false` otherwise.
     */
    fun onContinue(viewModel: UiStateViewModel, navController: NavController): Boolean = true

    @get:StringRes
    val nextButtonText: Int
        get() = R.string.common_continue

    companion object {
        val LIST = listOf(
            WelcomePage(),
            SendingCommandsPage(),
            ParametersPage(),
            ExamplesPage(),
            PermsPage(),
            CommandsPage(),
            PinPage(),
            FinishPage()
        )
    }
}
