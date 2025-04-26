package com.smscommands.app.utils

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.biometric.BiometricPrompt.AuthenticationResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


fun getBiometricPrompt(
    activity: FragmentActivity,
    onSuccess: () -> Unit,
    onFailure: () -> Unit,
    onError: (Int, CharSequence) -> Unit,
): () -> Unit {
    val executor = ContextCompat.getMainExecutor(activity)

    val authenticationCallback = object : AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: AuthenticationResult) { onSuccess() }
        override fun onAuthenticationFailed() { onFailure() }
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            onError(errorCode, errString)
        }
    }

    val prompt = BiometricPrompt(activity, executor, authenticationCallback)

    val promptDesc = BiometricManager.from(activity).getStrings(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)?.promptMessage

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Authenticate to continue")
        .setDescription(promptDesc)
        .setAllowedAuthenticators(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
        .build()

    return { prompt.authenticate(promptInfo) }
}