package com.smscommands.app.ui.screens.onboarding

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smscommands.app.R
import com.smscommands.app.ui.components.BoldedKeyValue

class CommandsPage : OnboardingPage {
    override val title = R.string.screen_onboarding_command_title

    @Composable
    override fun Content() {
        Text(
            text = "To use a command, text yourself a message with the following structure",
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "!!{PIN}/{command}/{parameters}",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        HorizontalDivider()

        BoldedKeyValue(
            key = "!!",
            value = "This prefix is required for the app to process the command.",
            modifier = Modifier.padding(top = 8.dp)
        )

        BoldedKeyValue(
            key = "{PIN}",
            value = "Your access pin, if any, you can change it at any time on the home screen.",
            modifier = Modifier.padding(top = 8.dp)
        )

        BoldedKeyValue(
            key = "{command}",
            value = "The command you want to execute, it must be enabled in the Commands screen",
            modifier = Modifier.padding(top = 8.dp)
        )

        BoldedKeyValue(
            key = "{parameters}",
            value = "The parameters for the command, if any, separated by spaces. More about them on the next page",
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}