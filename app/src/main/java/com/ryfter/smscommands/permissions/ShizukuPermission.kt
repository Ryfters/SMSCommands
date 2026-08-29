package com.ryfter.smscommands.permissions

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.ryfter.smscommands.R
import com.ryfter.smscommands.ui.navigation.LocalNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.navigation.navigate
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku
import kotlin.time.Duration.Companion.minutes

class ShizukuPermission : Permission {
    override val id = "rikka.shizuku"

    override val label = R.string.permission_shizuku

    override fun isGranted(context: Context): Boolean =
        Shizuku.pingBinder() && Shizuku.checkSelfPermission() == PERMISSION_GRANTED


    @Composable
    override fun request(onResult: (Boolean) -> Unit): () -> Unit {
        val scope = rememberCoroutineScope()
        val backStack = LocalNavBackStack.current

        return {
            if (!Shizuku.pingBinder()) {
                backStack.navigate(Route.Perms.ShizukuNotFoundDialog)
            } else {
                var job: Job? = null

                val listener = object : Shizuku.OnRequestPermissionResultListener {
                    override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
                        onResult(grantResult == PERMISSION_GRANTED)
                        Shizuku.removeRequestPermissionResultListener(this)
                        job?.cancel()
                    }
                }

                Shizuku.addRequestPermissionResultListener(listener)
                Shizuku.requestPermission(0)

                job = scope.launch {
                    delay(1.minutes)
                    Shizuku.removeRequestPermissionResultListener(listener)
                }
            }
        }
    }
}
