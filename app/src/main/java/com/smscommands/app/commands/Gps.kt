package com.smscommands.app.commands

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.smscommands.app.R
import com.smscommands.app.permissions.Permission
import com.smscommands.app.utils.formatRelativeTime
import java.time.Instant

class Gps : Command {
    override val id = "command_gps"
    override val label = R.string.command_gps_label
    override val description = R.string.command_gps_desc
    override val usage = R.string.command_gps_usage

    override val requiredPermissions = listOf(
        Permission.LOCATION
    )

    @SuppressLint("MissingPermission") // Already checked in .receiver.processCommands()
    override fun onReceive(context: Context, args: List<String>, sender: String, onReply: (String) -> Unit) {


        var bestLocation: Location? = null

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        providers.forEach { provider ->
            val providerLocation = locationManager.getLastKnownLocation(provider) ?: return@forEach

            if (bestLocation == null || providerLocation.accuracy < bestLocation.accuracy) {
                bestLocation = providerLocation
            }
        }

        if (bestLocation == null) {
            onReply(context.getString(R.string.command_gps_reply_error))
            return
        }

        val mapsUrl = context.getString(
            R.string.url_maps,
            bestLocation.latitude,
            bestLocation.longitude,
        )

        val formattedTime = formatRelativeTime(context, Instant.ofEpochMilli(bestLocation.time))

        onReply(
            context.getString(
                R.string.command_gps_reply,
                mapsUrl,
                bestLocation.accuracy.toInt(),
                formattedTime
            )
        )
    }
}