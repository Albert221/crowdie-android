package me.wolszon.groupie.android

import android.content.Context
import android.content.SharedPreferences
import me.wolszon.groupie.R
import me.wolszon.groupie.api.domain.Preferences
import javax.inject.Inject

class AndroidPreferences @Inject constructor(private val context: Context) : Preferences {
    private var preferences: SharedPreferences =
            context.getSharedPreferences(javaClass.simpleName, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_USERNAME = "username"
    }

    override var username: String
        get() = preferences.getString(PREF_USERNAME, context.resources.getString(R.string.no_name))
        set(value) = preferences.edit().putString(PREF_USERNAME, value).apply()
}