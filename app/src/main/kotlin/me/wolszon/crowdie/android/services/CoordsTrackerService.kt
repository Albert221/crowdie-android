package me.wolszon.crowdie.android.services

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
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationCallback as GoogleLocationCallback
import me.wolszon.crowdie.R
import me.wolszon.crowdie.android.ui.group.GroupActivity
import me.wolszon.crowdie.base.BaseService
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.support.annotation.RequiresApi


class CoordsTrackerService : BaseService(), CoordsTrackerView {
    @Inject lateinit var presenter: CoordsTrackerPresenter
    private lateinit var notification: Notification
    private lateinit var locationProvider: FusedLocationProviderClient
    private val locationCallback: GoogleLocationCallback by lazy { LocationCallback() }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "groupie"

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(resources.getString(R.string.app_name))
                .setContentText(resources.getString(R.string.service_notification_text))
                .setSmallIcon(android.R.drawable.ic_dialog_map)
                .setOngoing(true)
                .setContentIntent(activityPendingIntent)
                .build()

        val locationRequest = LocationRequest.create()
        locationRequest.interval = TimeUnit.SECONDS.toMillis(5)
        locationProvider = LocationServices.getFusedLocationProviderClient(this)

        val checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            return
        }

        locationProvider.requestLocationUpdates(locationRequest, locationCallback, null)

        startForeground(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Localization"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        presenter.subscribe(this)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        locationProvider.removeLocationUpdates(locationCallback)
        presenter.unsubscribe()
    }

    override fun stopService() {
        CoordsTrackerService.stop(this)
    }

    override fun showErrorDialog(e: Throwable) {
        Log.e("COORDS", e.toString())
    }

    private inner class LocationCallback : GoogleLocationCallback() {
        override fun onLocationResult(location: LocationResult) {
            super.onLocationResult(location)

            presenter.sendCoords(location)
        }
    }
}