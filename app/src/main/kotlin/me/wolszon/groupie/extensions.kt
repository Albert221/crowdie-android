package me.wolszon.groupie

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