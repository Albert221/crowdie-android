package me.wolszon.groupie.api.models.dataclass

import com.google.android.gms.maps.model.LatLng
import me.wolszon.groupie.android.GroupieApplication
import java.util.*

data class Member (
        val id: String,
        val name: String,
        val role: Int,
        val lat: Float,
        val lng: Float,
        val androidId: String
) {
    private var distance = Random().nextInt(15_000)

    companion object {
        const val MEMBER = 0
        const val ADMIN = 1
    }

    fun getLatLng(): LatLng = LatLng(lat.toDouble(), lng.toDouble())

    fun isYou(): Boolean = androidId == GroupieApplication.androidId

    /**
     * Returns distanceFromUser from user in meters.
     */
    fun distanceFromUser(): Int = if (isYou()) 0 else distance
}