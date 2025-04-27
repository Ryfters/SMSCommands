package com.smscommands.app.ui.screens.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smscommands.app.R
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.components.MainScaffold
import com.smscommands.app.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: UiStateViewModel
) {
    MainScaffold(
        navController = navController,
        title = "History",
        actions = {
            IconButton(
                onClick = { navController.navigate(Routes.History.CLEAR_DIALOG) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_history_delete),
                    contentDescription = null
                )
            }
        },
        showUpButton = true,
    ) {
        val historyEnabled by viewModel.historyEnabled.collectAsState()
        val history by viewModel.history.collectAsState()

        if (!historyEnabled) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 42.dp)
            ) {
                Text(
                    text = stringResource(R.string.screen_history_error_not_enabled),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    textAlign = TextAlign.Center
                )
            }
            return@MainScaffold
        }

        if (history.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 42.dp)
            ) {
                Text(
                    text = stringResource(R.string.screen_history_error_empty),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
                )
            }
            return@MainScaffold
        }

        LazyColumn {
            items(history) { historyItem ->
                HistoryItem(historyItem,
                    onInfoPressed = {
                        navController.navigate(Routes.History.ITEM_DIALOG + historyItem.id)
                    }
                )
            }
        }
    }
}

