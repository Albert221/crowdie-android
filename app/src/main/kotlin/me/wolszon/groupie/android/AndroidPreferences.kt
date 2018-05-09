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
        private const val PREF_LAST_JOINED_GROUP_TOKEN = "last_joined_group_token"
        private const val PREF_LAST_JOINED_GROUP_MEMBER_ID = "last_joined_group_member_id"
    }

    override var username: String
        get() = preferences.getString(PREF_USERNAME, "")
        set(value) = preferences.edit().putString(PREF_USERNAME, value).apply()

    override var lastJoinedGroup: String?
        get() = preferences.getString(PREF_LAST_JOINED_GROUP, null)
        set(value) = preferences.edit().putString(PREF_LAST_JOINED_GROUP, value).apply()

    override var lastJoinedGroupToken: String?
        get() = preferences.getString(PREF_LAST_JOINED_GROUP_TOKEN, null)
        set(value) = preferences.edit().putString(PREF_LAST_JOINED_GROUP_TOKEN, value).apply()

    override var lastJoinedGroupMemberId: String?
        get() = preferences.getString(PREF_LAST_JOINED_GROUP_MEMBER_ID, null)
        set(value) = preferences.edit().putString(PREF_LAST_JOINED_GROUP_MEMBER_ID, value).apply()
}