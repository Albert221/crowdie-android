package me.wolszon.groupie.utils

object DistanceFormatter {
    fun format(meters: Int): String = when (meters) {
        in 0..1_000 -> "%dm".format(meters)
        in 1_001..10_000 -> "%.1fkm".format(meters.toFloat() / 1_000).replace(",", ".")
        else -> "%dkm".format(meters / 1_000)
    }
}