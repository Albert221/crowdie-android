package me.wolszon.crowdie.utils

import java.lang.Math.*

object Math {
    const val R = 6372800 // in meters

    fun haversine(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val lambda1 = toRadians(lat1)
        val lambda2 = toRadians(lat2)
        val delta1 = toRadians(lat2 - lat1)
        val delta2 = toRadians(lng2 - lng1)
        return 2 * R * asin(sqrt(pow(sin(delta1 / 2), 2.0) + pow(sin(delta2 / 2), 2.0) * cos(lambda1) * cos(lambda2)))
    }
}