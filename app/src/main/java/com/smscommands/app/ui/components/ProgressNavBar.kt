package com.smscommands.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProgressNavBar(
    nextContent: String,
    onNextClicked: () -> Unit,
    pageIndex: Int,
    pageCount: Int,
    pageOffset: Float = 0f,

) {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(IntrinsicSize.Min)
    ) {

        DotProgressIndicator(pageIndex, pageCount, pageOffset, Modifier)

        Button(
            onClick = onNextClicked,
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) { Text(nextContent) }
    }
}