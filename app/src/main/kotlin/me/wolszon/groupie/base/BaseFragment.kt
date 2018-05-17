package me.wolszon.groupie.base

import android.util.Log
import dagger.android.support.DaggerFragment
import me.wolszon.groupie.android.GroupieApplication

abstract class BaseFragment : DaggerFragment() {
    fun showErrorDialog(e: Throwable) {
        Log.e(GroupieApplication.APPLICATION_NAME, "An error has occured.", e)
    }
}