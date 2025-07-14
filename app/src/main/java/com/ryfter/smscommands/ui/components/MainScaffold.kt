package com.ryfter.smscommands.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
    showUpButton: Boolean = false,
    onUpButtonClicked: () -> Unit = { navController.navigateUp() },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    consumeNavPadding: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = title,
                subtitle = subtitle,
                showUpButton = showUpButton,
                onUpButtonClicked = onUpButtonClicked,
                actions = actions,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        val newPadding = PaddingValues(top = padding.calculateTopPadding())
        Column(
            modifier = modifier.padding(newPadding),
        ) {
            content()
            if (consumeNavPadding)
                Spacer(Modifier.padding(WindowInsets.navigationBars.asPaddingValues()))
        }
    }
}
