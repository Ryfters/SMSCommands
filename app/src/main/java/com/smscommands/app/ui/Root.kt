package com.smscommands.app.ui

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.data.db.HistoryDatabase
import com.smscommands.app.data.db.HistoryRepository
import com.smscommands.app.ui.navigation.NavGraph
import com.smscommands.app.ui.navigation.dataStore
import com.smscommands.app.ui.theme.SMSCommandsTheme

@Composable
fun Root() {

    val context: Context = LocalContext.current

    val dataStore: DataStore<Preferences> = context.dataStore

    val database = HistoryRepository(
        HistoryDatabase.getDatabase(context).historyDao()
    )

    val extras = MutableCreationExtras().apply {
        set(UiStateViewModel.DATASTORE_KEY, dataStore)
        set(UiStateViewModel.DATABASE_KEY, database)
    }

    val viewModel: UiStateViewModel = viewModel(
        factory = UiStateViewModel.Factory,
        extras = extras
    )


    val dynamicColors by viewModel.dynamicColorsEnabled.collectAsState()

    val darkThemeValue by viewModel.darkThemeType.collectAsState()
    val darkTheme = when (darkThemeValue) {
        1 -> false
        2 -> true
        else -> isSystemInDarkTheme()
    }

    SMSCommandsTheme(
        dynamicColor = dynamicColors,
        darkTheme = darkTheme
    ) {
        NavGraph(
            navController = rememberNavController(),
            viewModel = viewModel
        )
    }

}

