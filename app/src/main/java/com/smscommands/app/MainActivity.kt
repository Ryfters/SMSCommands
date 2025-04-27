package com.smscommands.app

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.data.db.HistoryDatabase
import com.smscommands.app.data.db.HistoryRepository
import com.smscommands.app.ui.Root
import java.time.Instant

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userPrefs")

class MainActivity : FragmentActivity() {

    private lateinit var viewModel: UiStateViewModel
    private lateinit var lastUsed: Instant

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lastUsed = Instant.now()

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
        if (Instant.now().isAfter(lastUsed.plusSeconds(300)))
            viewModel.updateSignedIn(false)
        viewModel.refreshPermissionsState(this)
    }

    override fun onPause() {
        super.onPause()
        lastUsed = Instant.now()
    }
}
