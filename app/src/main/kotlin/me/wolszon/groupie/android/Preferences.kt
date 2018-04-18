package me.wolszon.groupie.android

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

interface Preferences {
    var username: String
}

class AndroidPreferences @Inject constructor(context: Context) : Preferences {
    private var preferences: SharedPreferences =
            context.getSharedPreferences(javaClass.simpleName, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_USERNAME = "username"
    }

    override var username: String
        get() = preferences.getString(PREF_USERNAME, "No name")
        set(value) = preferences.edit().putString(PREF_USERNAME, value).apply()
}