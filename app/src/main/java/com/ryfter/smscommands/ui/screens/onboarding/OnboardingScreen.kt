package com.ryfter.smscommands.ui.screens.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.MyNavBackStack

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OnboardingScreen(
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel,
) {
    var pageIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentPage = OnboardingPage.LIST[pageIndex]

    val pageTitle = stringResource(currentPage.title)

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            Column {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = pageTitle,
                        )
                    },
                    expandedHeight = 224.dp,
                    scrollBehavior = scrollBehavior
                )

                val progress by animateFloatAsState(targetValue = (pageIndex + 1f) / OnboardingPage.LIST.size)
                LinearProgressIndicator(
                    progress = { progress },
                    drawStopIndicator = {},
                    modifier = Modifier
                        .scale(scaleX = 1.01f, scaleY = 1f)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        },

    ) { padding ->
        var expanded by remember { mutableStateOf(true) }
        val scrollState = rememberScrollState()

        var prevScrollState by remember { mutableIntStateOf(scrollState.value) }
        var prevTopAppBarHeight by remember { mutableFloatStateOf(scrollBehavior.state.heightOffset) }

        LaunchedEffect(scrollBehavior.state.heightOffset) {
            if (scrollBehavior.state.heightOffset > prevTopAppBarHeight) expanded = true
            else if (scrollBehavior.state.heightOffset < prevTopAppBarHeight) expanded = false

            prevTopAppBarHeight = scrollBehavior.state.heightOffset
        }

        LaunchedEffect(scrollState.value) {
            if (scrollState.value > prevScrollState) expanded = false
            else if (scrollState.value < prevScrollState) expanded = true

            prevScrollState = scrollState.value
        }

        currentPage.Content(
            viewModel = viewModel,
            backStack = backStack,
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(scrollState)
                .fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())

        ) {

            val targetOffset =
                (-ScreenOffset - padding.calculateBottomPadding()) * if (expanded) 1 else -2
            val offset by animateDpAsState(targetOffset)
            HorizontalFloatingToolbar(
                expanded = false,
                contentPadding = PaddingValues(10.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = offset),
            ) {
                SplitButtonLayout(
                    leadingButton = {
                        SplitButtonDefaults.LeadingButton(
                            onClick = { pageIndex -= 1 },
                            shapes = SplitButtonDefaults.leadingButtonShapesFor(SplitButtonDefaults.MediumContainerHeight),
                            colors = ButtonDefaults.filledTonalButtonColors(),
                            enabled = pageIndex != 0
                        ) {
                            Icon(painterResource(R.drawable.ic_back), null)
                        }
                    },
                    trailingButton = {
                        SplitButtonDefaults.TrailingButton(
                            onClick = {
                                if (currentPage.onContinue(viewModel, backStack)) pageIndex += 1
                            },
                            shapes = SplitButtonDefaults.trailingButtonShapesFor(SplitButtonDefaults.MediumContainerHeight),
                            colors = ButtonDefaults.filledTonalButtonColors(),
                        ) {
                            Text(
                                text = stringResource(currentPage.nextButtonText),
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    },
                    // it fills max width, then it scales, so this is the equivalent of .fillMaxWidth(.7f)
                    modifier = Modifier
                        .scale(1.4f)
                        .fillMaxWidth(.5f),
                )
            }
        }
    }
}