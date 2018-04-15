package me.wolszon.groupie.android.services

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import me.wolszon.groupie.android.ui.group.GroupActivity
import me.wolszon.groupie.base.BaseService
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CoordsTrackerService : BaseService(), CoordsTrackerView {
    @Inject lateinit var presenter: CoordsTrackerPresenter
    private lateinit var notification: Notification

    companion object {
        const val NOTIFICATION_CHANNEL = "groupie"

        fun start(context: Context) {
            context.startService(createIntent(context))
        }

        fun stop(context: Context) {
            context.stopService(createIntent(context))
        }

        private fun createIntent(context: Context): Intent =
                Intent(context, CoordsTrackerService::class.java)
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        val activityPendingIntent = PendingIntent.getActivity(this, 0, GroupActivity.createIntent(this).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }, 0)

        notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                .setContentTitle("Groupie")
                .setContentText("Twoja lokalizacja jest przekazywana do grupy")
                .setSmallIcon(android.R.drawable.ic_dialog_map)
                .setOngoing(true)
                .setContentIntent(activityPendingIntent)
                .build()

        val locationRequest = LocationRequest.create()
        locationRequest.interval = TimeUnit.SECONDS.toMillis(5)
        val client = LocationServices.getFusedLocationProviderClient(this)

        val checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            return
        }

        client.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(location: LocationResult) {
                super.onLocationResult(location)

                presenter.sendCoords(location)
            }
        }, null)

        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        presenter.subscribe(this)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.unsubscribe()
    }

    override fun showErrorDialog(e: Throwable) {
        Log.e("COORDS", e.toString())
    }
}