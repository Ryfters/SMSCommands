package com.smscommands.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import kotlin.math.absoluteValue


@Composable
fun DotProgressIndicator(
    currentPage: Int,
    totalScreens: Int,
    pageOffset: Float,
    modifier: Modifier = Modifier
) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = MaterialTheme.colorScheme.primaryContainer

    val dotRadius = 9f
    val dotSpacing = 30f

    val totalWidth = dotSpacing * (totalScreens - 1) + dotRadius * 2

    // Inverting the length 1 page /2 so animation doesnt switch from -.5 to .5 when page changes
    val sign = if (currentPage % 2 == 0) 1 else -1

    val animatedValue = remember { Animatable(0f) }
    val lineLength = animatedValue.value * sign

    val targetLength = when {
        pageOffset < -0.1f -> -dotSpacing
        pageOffset > 0.1f -> dotSpacing
        else -> 0f
    }

    LaunchedEffect(targetLength) {
        animatedValue.animateTo(targetLength * sign)
    }

    // Forcing minimum line length to stop jumps whenever it switches before reaching max length
    LaunchedEffect(pageOffset) {
        if (pageOffset.absoluteValue > 0.25f) {
            val minOffset = (pageOffset.absoluteValue - 0.25f) * 4 * targetLength * sign
            if (animatedValue.value.absoluteValue < minOffset.absoluteValue) {
                animatedValue.snapTo(minOffset)
            }
        }
    }


    Canvas(
        modifier = modifier
            .fillMaxHeight()
    ) {

        for (i in 0 until totalScreens) {
            val xOffset = (i * dotSpacing) - totalWidth/2
            val offset = Offset(xOffset, this.center.y)
            drawCircle(
                color = unselectedColor,
                radius = dotRadius,
                center = offset
            )
        }

        val xOffset = (currentPage * dotSpacing) - totalWidth/2
        val lineStartOffset = Offset(xOffset, this.center.y)
        val lineEndOffset = Offset(lineStartOffset.x + lineLength, this.center.y)

        drawLine(
            color = selectedColor,
            start = lineStartOffset,
            end = lineEndOffset,
            strokeWidth = dotRadius * 2,
            cap = StrokeCap.Round
        )
    }
}

