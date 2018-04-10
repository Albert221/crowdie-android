package me.wolszon.groupie.android

import android.annotation.SuppressLint
import android.provider.Settings
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import me.wolszon.groupie.di.DaggerAppComponent

class GroupieApplication : DaggerApplication() {
    companion object {
        const val API_BASE_URL = "http://192.168.1.30:8080"
        lateinit var androidId: String
    }

    @SuppressLint("HardwareIds")
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        androidId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)

        return DaggerAppComponent.builder().create(this)
    }
}
