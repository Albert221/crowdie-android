package me.wolszon.groupie.api.models.dataclass

import com.google.android.gms.maps.model.LatLng
import me.wolszon.groupie.android.GroupieApplication
import me.wolszon.groupie.api.state.GroupState.currentUser
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

        val distanceCache = hashMapOf<Int, Int>()
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

        if (distanceCache.containsKey(hashCode())) {
            return distanceCache[hashCode()]!!
        }

        val distance = Math.haversine(
                lat.toDouble(),
                lng.toDouble(),
                currentUser.lat.toDouble(),
                currentUser.lng.toDouble()
        ).roundToInt()

        distanceCache[hashCode()] = distance

        return distance
    }
}