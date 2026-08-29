package com.ryfter.smscommands.shizuku

import android.content.ComponentName
import android.content.ServiceConnection
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.IBinder
import android.util.Log
import com.ryfter.smscommands.BuildConfig
import com.ryfter.smscommands.service.IShizukuService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import rikka.shizuku.Shizuku
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.time.Duration.Companion.minutes

class ShizukuManager {



    fun getShizukuStatus(): ShizukuStatus {
        if (!Shizuku.pingBinder()) return ShizukuStatus.BINDER_DEAD
        if (Shizuku.checkSelfPermission() != PERMISSION_GRANTED) return ShizukuStatus.MISSING_PERM
        return ShizukuStatus.OK
    }
    fun isGranted(): Boolean =
        getShizukuStatus() == ShizukuStatus.OK


    suspend fun <T>usingShizuku(
        action: (service: IShizukuService?) -> T
    ) : T {

        if (!isGranted()) {
            Log.w(TAG, "usingShizuku: shizuku status not ok, ShizukuStatus=${getShizukuStatus()}")
            return action(null)
        }


        var onUnbindService: () -> Unit = {}
        try {
            return suspendCancellableCoroutine { continuation ->
                val args = Shizuku.UserServiceArgs(
                    ComponentName(
                        BuildConfig.APPLICATION_ID,
                        ShizukuService::class.java.name
                    )
                ).daemon(false)
                    .processNameSuffix("shizukuService")
                    .debuggable(BuildConfig.DEBUG)
                    .version(BuildConfig.VERSION_CODE)

                val connection = object : ServiceConnection {
                    override fun onServiceConnected(
                        name: ComponentName?,
                        binder: IBinder?
                    ) {
                        Log.i(TAG, "onServiceConnected")

                        try {
                            val service = IShizukuService.Stub.asInterface(binder)
                            continuation.resume(action(service))
                        } catch(e: Exception) {
                            continuation.resumeWithException(e)
                        }
                    }

                    override fun onServiceDisconnected(name: ComponentName?) {
                        Log.i(TAG, "onServiceDisconnected")
                    }
                }

                onUnbindService = { Shizuku.unbindUserService(args, connection, true) }
                Shizuku.bindUserService(args, connection)
            }
        } catch(e: Exception) {
            Log.i(TAG, "usingShizuku: Exception:", e)
            return action(null)
        } finally {
            try { onUnbindService() }
            catch(e: Exception) {
                Log.w(TAG, "usingShizuku: Couldn't unbind service", e)
            }
        }
    }


    fun request(onResult: (Boolean) -> Unit) {

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

        job = CoroutineScope(Dispatchers.IO).launch {
            delay(1.minutes)
            Shizuku.removeRequestPermissionResultListener(listener)
        }
    }

    companion object {
        private const val TAG = "ShizukuService"

        enum class ShizukuStatus {
            OK,
            MISSING_PERM,
            BINDER_DEAD
        }
    }
}