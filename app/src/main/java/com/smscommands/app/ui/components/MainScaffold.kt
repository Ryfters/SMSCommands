package com.smscommands.app.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
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
    onUpButtonClicked: () -> Unit = {
        navController.navigateUp()
    },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
    content: @Composable () -> Unit,
) {

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
        // Removes the bottom padding so it doesn't cut off the gesture bar
        val newPadding = PaddingValues(top = padding.calculateTopPadding())
        Log.d("newPadding", newPadding.toString())
        Box(
            modifier = Modifier.padding(newPadding)
        ) {
            content()
        }
    }
}
