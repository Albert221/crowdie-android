package me.wolszon.groupie.api.models.dataclass

import com.google.android.gms.maps.model.LatLng
import me.wolszon.groupie.android.GroupieApplication
import me.wolszon.groupie.utils.CurrentPositionUtil
import me.wolszon.groupie.utils.Math
import kotlin.math.roundToInt

data class Member (
        val id: String,
        val name: String,
        val role: Int,
        val lat: Float,
        val lng: Float,
        val androidId: String
) {
    companion object {
        const val MEMBER = 0
        const val ADMIN = 1
    }

    fun getLatLng(): LatLng = LatLng(lat.toDouble(), lng.toDouble())

    fun isYou(): Boolean = androidId == GroupieApplication.androidId

    /**
     * Returns distanceFromUser from user in meters.
     */
    fun distanceFromUser(): Int {
        if (isYou()) {
            return 0
        }

        val distance = Math.haversine(
                lat.toDouble(),
                lng.toDouble(),
                CurrentPositionUtil.latitude.toDouble(),
                CurrentPositionUtil.longitude.toDouble()
        ).roundToInt()

        return distance
    }
}