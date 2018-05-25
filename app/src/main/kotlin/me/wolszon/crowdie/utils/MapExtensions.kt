package me.wolszon.crowdie.utils

import android.content.Context
import android.support.annotation.DrawableRes
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat

fun bitmapDescriptorFactoryFromVectorResource(@DrawableRes drawable: Int, context: Context,
                                              @ColorInt tintColor: Int = 0xFFFFFF): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(context, drawable)
    vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)

    DrawableCompat.setTint(vectorDrawable, tintColor)

    val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}