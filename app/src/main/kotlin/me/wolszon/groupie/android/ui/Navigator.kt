package me.wolszon.groupie.android.ui

import android.app.Activity
import android.content.Intent
import me.wolszon.groupie.android.ui.group.GroupActivity
import me.wolszon.groupie.android.ui.group.GroupQrActivity

interface NavigatorInterface {
    fun openGroupActivity(groupId: String)
    fun openGroupQrActivity(groupId: String)
}

class Navigator(private val context: Activity) : NavigatorInterface {
    override fun openGroupActivity(groupId: String) {
        context.startActivity(GroupActivity.createIntent(context, groupId).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }

    override fun openGroupQrActivity(groupId: String) {
        context.startActivity(GroupQrActivity.createIntent(context, groupId))
    }
}