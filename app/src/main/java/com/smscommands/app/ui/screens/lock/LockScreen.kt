package com.smscommands.app.ui.screens.lock

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.utils.getBiometricPrompt

@Composable
fun LockScreen(
    viewModel: UiStateViewModel,
) {
    val activity = LocalActivity.current as FragmentActivity

    val signedIn by viewModel.signedIn.collectAsState()
    val requirePin by viewModel.requirePin.collectAsState()
    val shouldShowLockScreen = !signedIn && requirePin


    AnimatedVisibility(
        visible = shouldShowLockScreen,
        enter = slideInVertically(
            initialOffsetY = { -it },
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it },
        )
    ) {
        val promptBiometrics = remember {
            getBiometricPrompt(activity,
                onSuccess = { viewModel.updateSignedIn(true) },
                onFailure = {  },
                onError = { _, errorString ->
                    Toast.makeText(activity, errorString, Toast.LENGTH_SHORT).show()
                }
            )
        }

        val pin by viewModel.pin.collectAsState()

        val transition = this.transition
        LaunchedEffect(transition.currentState) {
            if (shouldShowLockScreen && transition.targetState == transition.currentState)
                promptBiometrics()
        }

        LockScreenContent(
            onShowBiometricPrompt = promptBiometrics,
            onPinUnlock = { pinInput, resetPin ->
                if (pinInput == pin) viewModel.updateSignedIn(true)
                else {
                    resetPin()
                    Toast.makeText(activity, "Incorrect PIN", Toast.LENGTH_SHORT).show()
                }
            }

        )
    }
}