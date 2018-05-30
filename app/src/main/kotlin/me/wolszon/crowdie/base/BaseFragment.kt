package me.wolszon.crowdie.base

import android.util.Log
import dagger.android.support.DaggerFragment
import me.wolszon.crowdie.android.Application

abstract class BaseFragment : DaggerFragment() {
    fun showErrorDialog(e: Throwable) {
        Log.e(Application.TAG, "An error has occured.", e)
    }
}