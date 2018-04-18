package me.wolszon.groupie.android.services

import com.google.android.gms.location.LocationResult
import me.wolszon.groupie.api.GroupManager
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.utils.CurrentPositionUtil

class CoordsTrackerPresenter(private val groupManager: GroupManager) : BasePresenter<CoordsTrackerView>() {
    fun sendCoords(location: LocationResult) {
        val latitude = location.lastLocation.latitude.toFloat()
        val longitude = location.lastLocation.longitude.toFloat()

        CurrentPositionUtil.latitude = latitude
        CurrentPositionUtil.longitude = longitude

        groupManager.sendCoords(latitude, longitude)
    }
}