package me.wolszon.crowdie.api.models.dataclass

import android.support.annotation.ColorInt
import com.google.android.gms.maps.model.LatLng
import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.utils.CurrentPositionUtil
import me.wolszon.crowdie.utils.Math
import kotlin.math.roundToInt

data class Member (
        val id: String,
        val name: String,
        val role: Role,
        val lat: Float,
        val lng: Float,
        @ColorInt
        val color: Int
) {
    enum class Role {
        MEMBER, ADMIN
    }

    fun isAdmin(): Boolean = role == Role.ADMIN

    fun getLatLng(): LatLng = LatLng(lat.toDouble(), lng.toDouble())

    fun isYou(): Boolean = id == GroupManager.state!!.currentMemberId

    /**
     * Returns distanceFromUser from user in meters.
     */
    fun distanceFromUser(): Int {
        if (isYou()) {
            return 0
        }

        return Math.haversine(
                lat.toDouble(),
                lng.toDouble(),
                CurrentPositionUtil.latitude.toDouble(),
                CurrentPositionUtil.longitude.toDouble()
        ).roundToInt()
    }
}