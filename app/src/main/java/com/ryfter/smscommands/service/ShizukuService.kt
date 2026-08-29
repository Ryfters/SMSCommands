package com.ryfter.smscommands.service

import android.content.Context
import android.system.Os
import android.util.Log
import androidx.annotation.Keep
import kotlin.system.exitProcess

class ShizukuService() : IShizukuService.Stub() {

    private var context: Context? = null

    @Keep
    constructor(context: Context) : this() {
        Log.i(TAG, "init with context")
        this.context = context
    }

    override fun destroy() {
        Log.i(TAG, "destroy")
        exitProcess(0)
    }

    override fun exit() {
        Log.i(TAG, "exit")
        destroy()
    }

    override fun logUserInfo() {
        Log.i(TAG, "UID=${Os.getuid()}, PID=${Os.getpid()}")
    }

    companion object {
        private const val TAG = "ShizukuService"
    }
}