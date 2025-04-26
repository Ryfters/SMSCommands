package com.smscommands.app.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.smscommands.app.R
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.components.ProgressNavBar
import com.smscommands.app.ui.navigation.Routes
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: UiStateViewModel,
) {

    val pageCount = OnboardingPages.size
    val pagerState = rememberPagerState { pageCount }

    val currentPage = pagerState.currentPage
    val pageOffset = pagerState.currentPageOffsetFraction

    val pageTitle = stringResource(OnboardingPages[currentPage].title)


    val onNextClicked = {
        viewModel.updateIsFirstLaunch(false)
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.HOME)
        }
    }

    val nextButton = @Composable { modifier: Modifier ->
        AnimatedContent(
            targetState = pagerState.canScrollForward,
            modifier = modifier
        ) { if (it)
                OutlinedButton(onNextClicked) {
                    Text(stringResource(R.string.common_skip))
                }
            else
                Button(onNextClicked) {
                    Text(stringResource(R.string.common_ok))
                }
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = pageTitle,
                        modifier = Modifier.alpha(1 - pageOffset.absoluteValue * 1.6f)
                    )
                },
                expandedHeight = 224.dp
            )
        },
        bottomBar = {
            ProgressNavBar(
                pageIndex = currentPage,
                pageCount = pageCount,
                pageOffset = pageOffset,
                nextButton = nextButton
            )
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) { page ->
            Text(
                text = stringResource(OnboardingPages[page].content),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

