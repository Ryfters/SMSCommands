package com.ryfter.smscommands.ui.screens.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatPreview(
    triggerMessage: String,
    responses: List<String>
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(
                text = triggerMessage,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(Modifier.padding(2.dp))

        responses.forEachIndexed { index, response ->
            Spacer(Modifier.padding(2.dp))

            var roundedCornerTop = 2.dp
            var roundedCornerBottom = 2.dp

            if (responses.lastIndex == index)
                roundedCornerBottom = 20.dp

            if (index == 0)
                roundedCornerTop = 20.dp

            val chatBubbleShape = RoundedCornerShape(
                topStart = 20.dp,
                bottomStart = 20.dp,
                topEnd = roundedCornerTop,
                bottomEnd = roundedCornerBottom
            )

            Row(modifier = Modifier.align(Alignment.End)) {
                Spacer(Modifier.fillMaxWidth(0.3f)) // So it can only fill up to 7/10th before wrapping to a new line
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = chatBubbleShape
                ) {
                    Text(
                        text = response,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}
