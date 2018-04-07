package me.wolszon.groupie.ui

import android.app.Activity
import me.wolszon.groupie.ui.group.GroupActivity
import me.wolszon.groupie.ui.groupqr.GroupQrActivity

interface NavigatorInterface {
    fun openGroupActivity()
    fun openGroupQrActivity(groupId: String)
}

class Navigator(private val context: Activity) : NavigatorInterface {
    override fun openGroupActivity() {
        context.startActivity(GroupActivity.createIntent(context))
    }

    override fun openGroupQrActivity(groupId: String) {
        context.startActivity(GroupQrActivity.createIntent(context, groupId))
    }
}