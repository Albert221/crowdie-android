package me.wolszon.crowdie.utils

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun RecyclerView.prepare() {
    layoutManager = LinearLayoutManager(context)
    drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
    isDrawingCacheEnabled = true
    setHasFixedSize(true)
}

fun ViewGroup.inflate(layoutId: Int): View =
        LayoutInflater.from(context).inflate(layoutId, this, false)

var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) { visibility = if (value) View.VISIBLE else View.GONE }

fun BottomNavigationView.setItemVisible(id: Int, visible: Boolean) {
    // https://stackoverflow.com/a/49666697/3158312
    // Terrible workaround, but what can I do...
    findViewById<View>(id).isVisible = visible
}

// https://stackoverflow.com/a/50393362/3158312
fun BottomNavigationView.disableShiftMode() {
    val menuView = getChildAt(0) as BottomNavigationMenuView

    val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
    shiftingMode.isAccessible = true
    shiftingMode.setBoolean(menuView, false)
    shiftingMode.isAccessible = false

    @SuppressLint("RestrictedApi")
    for (i in 0 until menuView.childCount) {
        (menuView.getChildAt(i) as BottomNavigationItemView).apply {
            setShiftingMode(false)
            setChecked(false)
        }
    }
}