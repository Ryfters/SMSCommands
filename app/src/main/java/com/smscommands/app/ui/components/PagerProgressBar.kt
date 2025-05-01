package com.smscommands.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PagerProgressBar(
    pagerState: PagerState,
    nextButton: @Composable (Modifier) -> Unit,
) {
    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(IntrinsicSize.Min)
    ) {
        DotProgressIndicator(pagerState)
//        PagerDots(pagerState, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
        nextButton(Modifier.align(Alignment.CenterEnd))
    }
}