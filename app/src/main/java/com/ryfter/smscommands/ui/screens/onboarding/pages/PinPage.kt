package com.ryfter.smscommands.ui.screens.onboarding.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.screens.onboarding.OnboardingPage

class PinPage : OnboardingPage {
    override val title = R.string.screen_onboarding_pin_title

    private var pinValue by mutableStateOf("")

    @Composable
    override fun Content(
        viewModel: UiStateViewModel,
        backStack: MyNavBackStack,
        modifier: Modifier
    ) {
        Column(modifier.padding(horizontal = 16.dp)) {
            Text(stringResource(R.string.screen_onboarding_pin_info))

            HorizontalDivider(Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp))

            TextField(
                value = pinValue,
                onValueChange = { if (it.length <= 8) pinValue = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                textStyle = MaterialTheme.typography.displaySmall.copy(textAlign = TextAlign.Center),
                singleLine = true,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(.7f)
                    .padding(vertical = 32.dp)
            )
        }
    }

    override fun onContinue(viewModel: UiStateViewModel, backStack: MyNavBackStack): Boolean {
        viewModel.updatePin(pinValue)
        return true
    }


    override val nextButtonText: Int
        get() = if (pinValue.isBlank()) R.string.common_skip else R.string.common_confirm

}