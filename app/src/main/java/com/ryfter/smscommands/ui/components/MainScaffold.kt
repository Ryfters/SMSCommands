package com.ryfter.smscommands.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.pop

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ModifierParameter")
@Composable
fun MainScaffold(
    backStack: MyNavBackStack,
    title: String,
    modifier: Modifier = Modifier.verticalScroll(rememberScrollState()),
    subtitle: String? = null,
    actions: @Composable (RowScope.() -> Unit) = {},
    showUpButton: Boolean = false,
    onUpButtonClicked: () -> Unit = { backStack.pop() },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    consumeNavPadding: Boolean = true,
    content: @Composable (ColumnScope.() -> Unit),
) {
    Scaffold(
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
        Column(
            modifier = Modifier
                .padding(top = padding.calculateTopPadding())
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .then(modifier) // verticalScroll() needs to go after nestedScroll()
        ) {
            content()
            if (consumeNavPadding) Spacer(Modifier.padding(WindowInsets.navigationBars.asPaddingValues()))
        }
    }
}
