package me.wolszon.groupie.android

import android.annotation.SuppressLint
import android.provider.Settings
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import me.wolszon.groupie.di.DaggerAppComponent

class GroupieApplication : DaggerApplication() {
    companion object {
        // DigitalOcean API server
        const val API_BASE_URL = "http://139.59.147.215:8080"
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
