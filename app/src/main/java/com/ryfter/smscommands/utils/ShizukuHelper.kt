    package com.ryfter.smscommands.utils

    import android.content.ComponentName
    import android.content.ServiceConnection
    import android.content.pm.PackageManager.PERMISSION_GRANTED
    import android.os.IBinder
    import android.util.Log
    import com.ryfter.smscommands.BuildConfig
    import com.ryfter.smscommands.service.ShizukuService
    import kotlinx.coroutines.suspendCancellableCoroutine
    import com.ryfter.smscommands.service.IShizukuService
    import rikka.shizuku.Shizuku
    import kotlin.coroutines.resume
    import kotlin.coroutines.resumeWithException

    enum class ShizukuStatus {
        OK,
        MISSING_PERM,
        BINDER_DEAD
    }

    fun getShizukuStatus(): ShizukuStatus {
        if (!Shizuku.pingBinder()) return ShizukuStatus.BINDER_DEAD
        if (Shizuku.checkSelfPermission() != PERMISSION_GRANTED) return ShizukuStatus.MISSING_PERM
        return ShizukuStatus.OK
    }

    suspend fun <T>usingShizuku(
        action: (service: IShizukuService?) -> T
    ) : T {

        if (getShizukuStatus() != ShizukuStatus.OK) {
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

    private const val TAG = "ShizukuService"