package com.smscommands.app.utils

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.biometric.BiometricPrompt.AuthenticationResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

fun getBiometricStatus(context: Context): Int {
    val biometricManager = BiometricManager.from(context)
    return biometricManager.canAuthenticate(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
}

fun getBiometricPrompt(
    activity: ComponentActivity,
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
    onError: (Int, CharSequence) -> Unit,
): () -> Unit {
    val fragmentActivity = activity as FragmentActivity
    val executor = ContextCompat.getMainExecutor(activity)

    val authenticationCallback = object : AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: AuthenticationResult) { onSuccess() }
        override fun onAuthenticationFailed() { onFailure() }
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            onError(errorCode, errString)
        }
    }

    val prompt = BiometricPrompt(fragmentActivity, executor, authenticationCallback)

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Authenticate to continue")
        .setAllowedAuthenticators(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
        .build()

    return { prompt.authenticate(promptInfo) }
}