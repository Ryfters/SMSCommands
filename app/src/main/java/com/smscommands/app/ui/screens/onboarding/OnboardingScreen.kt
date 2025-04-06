package com.smscommands.app.ui.screens.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: UiStateViewModel,
) {
    val coroutineScope = rememberCoroutineScope()

    val pageCount = OnboardingPages.size
    val pagerState = rememberPagerState { pageCount }

    val currentPage = pagerState.currentPage
    val pageOffset = pagerState.currentPageOffsetFraction

    val pageTitle = stringResource(OnboardingPages[currentPage].title)
    val pageContent = stringResource(OnboardingPages[currentPage].content)

    val scrollToPage: (Int) -> Unit = { page ->
        coroutineScope.launch {
            pagerState.animateScrollToPage(page)
        }
    }


    val prevContent: String
    val onPrevClicked: () -> Unit

    if (pagerState.canScrollBackward) {
        prevContent = stringResource(R.string.common_back)
        onPrevClicked = { scrollToPage(currentPage - 1) }
    } else {
        prevContent = stringResource(R.string.common_skip)
        onPrevClicked = {
            viewModel.updateIsFirstLaunch(false)
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.HOME)
            }
        }
    }


    val nextContent: String
    val onNextClicked: () -> Unit

    if (pagerState.canScrollForward) {
        nextContent = stringResource(R.string.common_next)
        onNextClicked = { scrollToPage(currentPage + 1) }
    } else {
        nextContent = stringResource(R.string.common_ok)
        onNextClicked = {
            viewModel.updateIsFirstLaunch(false)
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.HOME)
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
            )
        },
        bottomBar = {
            ProgressNavBar(
                nextContent = nextContent,
                onNextClicked = onNextClicked,
                prevContent = prevContent,
                onPrevClicked = onPrevClicked,
                pageIndex = currentPage,
                pageCount = pageCount,
                pageOffset = pageOffset,
            )
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(
                text = pageContent,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

