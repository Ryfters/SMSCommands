package com.ryfter.smscommands.ui.screens.onboarding.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.Routes
import com.ryfter.smscommands.ui.screens.onboarding.OnboardingPage

class FinishPage : OnboardingPage {
    override val title = R.string.screen_onboarding_finish_title

    @Composable
    override fun Content(
        viewModel: UiStateViewModel,
        navController: NavController,
        modifier: Modifier
    ) {
        Column(modifier.padding(horizontal = 16.dp)) {
            Text(stringResource(R.string.screen_onboarding_finish_other_settings))
            Spacer(Modifier.height(4.dp))
            Text(stringResource(R.string.screen_onboarding_finish_suggest))
        }
    }

    override fun onContinue(viewModel: UiStateViewModel, navController: NavController): Boolean {
        navController.navigate(Routes.Home.MAIN)
        viewModel.updateIsFirstLaunch(false)
        return false
    }

    override val nextButtonText: Int
        get() = R.string.common_finish

}