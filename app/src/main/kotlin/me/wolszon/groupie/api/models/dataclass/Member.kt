package me.wolszon.groupie.api.models.dataclass

import com.google.android.gms.maps.model.LatLng

data class Member (
        val id: String,
        val name: String,
        val role: Int,
        val lat: Float,
        val lng: Float
) {
    companion object {
        const val MEMBER = 0
        const val ADMIN = 1
    }

    fun getLatLng(): LatLng = LatLng(lat.toDouble(), lng.toDouble())
}