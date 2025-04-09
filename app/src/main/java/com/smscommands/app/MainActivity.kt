package com.smscommands.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.data.db.HistoryDatabase
import com.smscommands.app.data.db.HistoryRepository
import com.smscommands.app.ui.Root
import com.smscommands.app.ui.navigation.dataStore


class MainActivity : ComponentActivity() {

    private lateinit var viewModel: UiStateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStore: DataStore<Preferences> = this.dataStore

        val database = HistoryRepository(
            HistoryDatabase.getDatabase(this).historyDao()
        )

        val extras = MutableCreationExtras().apply {
            set(UiStateViewModel.DATASTORE_KEY, dataStore)
            set(UiStateViewModel.DATABASE_KEY, database)
        }

        val viewModelStoreOwner: ViewModelStoreOwner = this

        viewModel = ViewModelProvider.create(
            viewModelStoreOwner,
            factory = UiStateViewModel.Factory,
            extras = extras
        )[UiStateViewModel::class]

        enableEdgeToEdge()
        setContent {
            Root(viewModel)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshPermissionsState(this)
    }
}

// TODO LIST
// Add Notification handling
// Better onboarding content text
// Handle params within the receiver
// Add permissionStates to viewModel,
//  then implement permission checks through the app
//  (commands, home)



// Maybe later
// Onboarding settings tile desc?



// Later
// Add custom color themes
// Redo icons

// Commands
// Status command
// Add a toggle command, wifi&data(with shizuku),
//  bluetooth, dnd, etc

