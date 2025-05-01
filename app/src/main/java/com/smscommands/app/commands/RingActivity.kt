package com.smscommands.app.commands

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.smscommands.app.BuildConfig
import com.smscommands.app.R
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RingActivity : ComponentActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var dialog: AlertDialog? = null
    private var stopDelay: Job? = null
    private var keepVolUpJob: Job? = null

    private val stopReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            finishAndRemoveTask()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(stopReceiver, IntentFilter(STOP_RINGING_INTENT), RECEIVER_EXPORTED)

        val ringTime = intent.getLongExtra(RING_TIME_EXTRA, 120_000)

        mediaPlayer = MediaPlayer.create(this, R.raw.ringtone)

        val audioManager = getSystemService(AudioManager::class.java)
        val mediaMaxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val setMaxStreamVolume = {
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                mediaMaxVol,
                0
            )
        }

        setMaxStreamVolume()

        mediaPlayer?.isLooping = true
        mediaPlayer?.start()

        stopDelay = GlobalScope.launch {
            delay(ringTime)
            finishAndRemoveTask()
        }

        keepVolUpJob = GlobalScope.launch {
            while (true) {
                delay(5_000)
                setMaxStreamVolume()
            }
        }

        dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.app_name))
            .setMessage(getString(R.string.command_ring_dialog_desc))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.common_stop)) { _, _ ->
                finishAndRemoveTask()
            }
            .show()

    }

    override fun onDestroy() {
        unregisterReceiver(stopReceiver)
        stopDelay?.cancel()
        keepVolUpJob?.cancel()
        mediaPlayer?.stop()
        dialog?.dismiss()
        super.onDestroy()
    }

    companion object {
        var STOP_RINGING_INTENT = "${BuildConfig.APPLICATION_ID}.RING_STOP"
        var RING_TIME_EXTRA = "ring_time"
    }
}