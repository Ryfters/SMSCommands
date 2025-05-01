package com.smscommands.app.ui.screens.onboarding

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smscommands.app.R
import com.smscommands.app.ui.components.BoldedKeyValue

class ParametersPage : OnboardingPage {
    override val title = R.string.screen_onboarding_parameters_title

    @Composable
    override fun Content() {

        Text("There are 2 types of parameters, flags and options")

        BoldedKeyValue(
            key = "Flags",
            value = "You just need include them in the parameters",
            modifier = Modifier.padding(top = 8.dp)
        )

        BoldedKeyValue(
            key = "Options",
            value = "They require an argument, and are formatted like this: 'parameter:argument'",
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}