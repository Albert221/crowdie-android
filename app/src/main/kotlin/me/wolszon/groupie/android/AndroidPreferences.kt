package me.wolszon.groupie.android

import android.content.Context
import android.content.SharedPreferences
import me.wolszon.groupie.api.domain.Preferences
import javax.inject.Inject

class AndroidPreferences @Inject constructor(context: Context) : Preferences {
    private var preferences: SharedPreferences =
            context.getSharedPreferences(javaClass.simpleName, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_USERNAME = "username"
        private const val PREF_LAST_JOINED_GROUP = "last_joined_group"
    }

    override var username: String
        get() = preferences.getString(PREF_USERNAME, "")
        set(value) = preferences.edit().putString(PREF_USERNAME, value).apply()

    override var lastJoinedGroup: String?
        get() = preferences.getString(PREF_LAST_JOINED_GROUP, null)
        set(value) = preferences.edit().putString(PREF_LAST_JOINED_GROUP, value).apply()
}