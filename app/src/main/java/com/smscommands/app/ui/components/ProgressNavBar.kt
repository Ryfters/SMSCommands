package com.smscommands.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProgressNavBar(
    nextContent: String,
    onNextClicked: () -> Unit,
    prevContent: String,
    onPrevClicked: () -> Unit,
    pageIndex: Int,
    pageCount: Int,
    pageOffset: Float = 0f,

) {
    Row (
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(IntrinsicSize.Min)
    ) {
        // TODO: Better animations
        // If i dont use boxes like that, buttons will push around the dot indicator when they change size
        Box(Modifier.weight(1f)) {
            Button(
                onClick = onPrevClicked,
                modifier = Modifier
                    .align(CenterStart)
            ) { Text(prevContent) }
        }

        DotProgressIndicator(pageIndex, pageCount, pageOffset)

        Box(Modifier.weight(1f)) {
            Button(
                onClick = onNextClicked,
                modifier = Modifier
                    .align(CenterEnd)
            ) { Text(nextContent) }
        }
    }
}