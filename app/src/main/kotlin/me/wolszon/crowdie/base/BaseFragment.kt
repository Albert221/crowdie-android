package me.wolszon.crowdie.base

import android.util.Log
import dagger.android.support.DaggerFragment
import me.wolszon.crowdie.android.CrowdieApplication

abstract class BaseFragment : DaggerFragment() {
    fun showErrorDialog(e: Throwable) {
        Log.e(CrowdieApplication.APPLICATION_NAME, "An error has occured.", e)
    }
}