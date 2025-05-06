package com.smscommands.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap


@Composable
fun DotProgressIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val totalPages = pagerState.pageCount

    if (totalPages <= 1) return

    val currentPage = pagerState.currentPage
    val pageOffset = pagerState.currentPageOffsetFraction

    val activeColor = MaterialTheme.colorScheme.primary
    val inactiveColor = MaterialTheme.colorScheme.primaryContainer

    val dotRadius = 9f
    val dotSpacing = 30f

    val totalWidth = dotSpacing * (totalPages - 1) + dotRadius * 2


    // Doing this ensures the animation has fully extended before retracting the other side
    // FIXME: getOffsetDistanceInPages is wrong?
    val animationTarget = -pagerState.getOffsetDistanceInPages(0)

    val animatedPosition = remember { Animatable(animationTarget) }
    LaunchedEffect(animationTarget) {
        animatedPosition.animateTo(animationTarget)
    }

    val constrainedAnimationValue =
        animatedPosition.value.coerceIn(animationTarget - 0.5f, animationTarget + 0.5f)

    val animatedCurrentOffset =
        (constrainedAnimationValue - constrainedAnimationValue.toInt()).let {
            if (it < 0.5) it.coerceAtMost(0.25f) * 4
            else (it - 1).coerceAtLeast(-0.25f) * 4
        }

    val animatedCurrentPage =
        if (animatedCurrentOffset < 0) constrainedAnimationValue.toInt() + 1
        else constrainedAnimationValue.toInt()



    Canvas(
        modifier = modifier
            .fillMaxHeight()
    ) {
        for (i in 0 until totalPages) {
            val xOffset = (i * dotSpacing) - totalWidth/2
            val offset = Offset(xOffset, this.center.y)
            drawCircle(
                color = inactiveColor,
                radius = dotRadius,
                center = offset
            )
        }

        val xStartOffset = (animatedCurrentPage * dotSpacing) - totalWidth / 2
        val xEndOffset =
            ((animatedCurrentPage + animatedCurrentOffset) * dotSpacing) - totalWidth / 2
        val lineStartOffset = Offset(xStartOffset, this.center.y)
        val lineEndOffset = Offset(xEndOffset, this.center.y)

        drawLine(
            color = activeColor,
            start = lineStartOffset,
            end = lineEndOffset,
            strokeWidth = dotRadius * 2,
            cap = StrokeCap.Round
        )
    }
}

