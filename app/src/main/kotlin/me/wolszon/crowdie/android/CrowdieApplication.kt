package me.wolszon.crowdie.android

import android.annotation.SuppressLint
import android.provider.Settings
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import me.wolszon.crowdie.di.DaggerAppComponent

class CrowdieApplication : DaggerApplication() {
    companion object {
        const val APPLICATION_NAME = "Crowdie"

        lateinit var androidId: String
    }

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()

        androidId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}
