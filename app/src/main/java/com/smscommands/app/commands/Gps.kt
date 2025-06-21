package com.smscommands.app.commands

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.smscommands.app.R
import com.smscommands.app.commands.params.ChoiceParamDefinition
import com.smscommands.app.data.SyncPreferences
import com.smscommands.app.permissions.Permission
import com.smscommands.app.utils.formatRelativeTime
import com.smscommands.app.utils.reply
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import java.time.Instant
import kotlin.coroutines.resume

class Gps : Command {
    override val id = "command_gps"
    override val label = R.string.command_gps_label
    override val description = R.string.command_gps_desc

    override val requiredPermissions = listOf(
        Permission.LOCATION
    )

    private val modeParamChoices = mapOf<Any, Int>(
        MODE_CURRENT to R.string.command_gps_param_mode_current,
        MODE_LAST_ACCURATE to R.string.command_gps_param_mode_last_accurate,
        MODE_LAST_RECENT to R.string.command_gps_param_mode_last_recent,
    )

    override val params = mapOf(
        MODE_PARAM to ChoiceParamDefinition(
            name = R.string.command_gps_param_mode,
            desc = R.string.command_gps_param_mode_desc,
            defaultValue = MODE_DEFAULT,
            choices = modeParamChoices
        ),
    )

    @SuppressLint("MissingPermission") // Already checked in .receiver.processCommands()
    override fun onReceive(
        context: Context,
        parameters: Map<String, Any?>,
        sender: String,
        id: Long?
    ) {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {

            val mode = parameters[MODE_PARAM] as String

            var syncPreferences = SyncPreferences.getPreferences(context)

            var bestLocation: Location? = null

            val locationManager = context.getSystemService(LocationManager::class.java)
            if (!locationManager.isLocationEnabled) {
                reply(
                    context,
                    context.getString(R.string.command_gps_reply_location_disabled),
                    sender,
                    id
                )
                id?.let {
                    syncPreferences.updateItemStatus(
                        it,
                        R.string.command_gps_error_location_disabled
                    )
                }
                return@launch
            }

            val providers = locationManager.getProviders(true)
            providers.ifEmpty {
                reply(
                    context,
                    context.getString(R.string.command_gps_reply_no_providers),
                    sender,
                    id
                )
                id?.let {
                    syncPreferences.updateItemStatus(
                        it,
                        R.string.command_gps_error_location_unavailabe
                    )
                }
                return@launch
            }


            if (mode == MODE_CURRENT || mode == MODE_DEFAULT) {

                val locations = mutableListOf<Location?>()

                withTimeout(5000L) {
                    suspendCancellableCoroutine<Unit> { continuation ->
                        providers.forEach { provider ->
                            locationManager.getCurrentLocation(
                                provider,
                                null,
                                context.mainExecutor
                            ) {
                                locations.add(it)
                                if (locations.size == providers.size) {
                                    locations.removeAll { it == null }
                                    continuation.resume(Unit)
                                }
                            }
                        }
                    }
                }

                locations
                    .filterNotNull()
                    .forEach { location ->
                        if (bestLocation == null || location.accuracy < bestLocation.accuracy)
                            bestLocation = location
                    }
            }

            if (
                mode == MODE_LAST_RECENT ||
                (mode == MODE_DEFAULT && bestLocation == null)
            ) {
                providers.forEach { provider ->
                    locationManager.getLastKnownLocation(provider)?.let { location ->
                        if (bestLocation == null || location.time > bestLocation!!.time)
                            bestLocation = location
                    }
                }

            }

            if (mode == MODE_LAST_ACCURATE) {
                providers.forEach { provider ->
                    locationManager.getLastKnownLocation(provider)?.let { location ->
                        if (bestLocation == null || location.accuracy < bestLocation!!.accuracy)
                            bestLocation = location
                    }
                }
            }


            if (bestLocation == null) {
                reply(context, context.getString(R.string.command_gps_reply_error), sender, id)
                id?.let {
                    syncPreferences.updateItemStatus(
                        it,
                        R.string.command_gps_error_location_unavailabe
                    )
                }
            } else {
                val mapsUrl = context.getString(
                    R.string.url_maps,
                    bestLocation.latitude,
                    bestLocation.longitude,
                )

                val formattedTime =
                    formatRelativeTime(context, Instant.ofEpochMilli(bestLocation.time))

                reply(
                    context,
                    context.getString(
                        R.string.command_gps_reply,
                        mapsUrl,
                        bestLocation.accuracy.toInt(),
                        formattedTime
                    ),
                    sender,
                    id
                )
            }
        }
    }

    companion object {
        const val MODE_PARAM = "mode_param"
        const val MODE_DEFAULT = "default_location"
        const val MODE_CURRENT = "current_location"
        const val MODE_LAST_ACCURATE = "last_location_accurate"
        const val MODE_LAST_RECENT = "last_location_recent"
    }
}