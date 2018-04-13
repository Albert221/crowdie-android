package me.wolszon.groupie.android.services

import android.Manifest
import android.app.Notification
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
import me.wolszon.groupie.base.BaseService
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CoordsTrackerService : BaseService(), CoordsTrackerView {
    @Inject lateinit var presenter: CoordsTrackerPresenter
    private lateinit var notification: Notification

    companion object {
        const val NOTIFICATION_CHANNEL = "groupie"
        const val EXTRA_MEMBER_ID = "MEMBER_ID"

        fun createIntent(context: Context, memberId: String): Intent {
            return Intent(context, CoordsTrackerService::class.java).apply {
                putExtra(EXTRA_MEMBER_ID, memberId)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                .setContentTitle("Groupie")
                .setContentText("Twoja lokalizacja jest przekazywana do grupy")
                .setSmallIcon(android.R.drawable.ic_dialog_map)
                .setOngoing(true)
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
        presenter.memberId = intent.getStringExtra(EXTRA_MEMBER_ID)
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