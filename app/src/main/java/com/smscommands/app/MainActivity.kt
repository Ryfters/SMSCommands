package com.smscommands.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.smscommands.app.ui.Root

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Root()
        }
    }
}

// TODO LIST
// Add Notification handling
// Better onboarding content text
// Handle params within the receiver
// Add permissionStates to viewModel,
//  then implement permission checks through the app

// Maybe later
// Onboarding settings tile desc?



// Later
// Add custom color themes
// Redo icons

// Commands
// Status command
// Add a toggle command, wifi&data(with shizuku),
//  bluetooth, dnd, etc

