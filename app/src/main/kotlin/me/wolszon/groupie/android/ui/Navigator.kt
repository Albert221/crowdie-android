package me.wolszon.groupie.android.ui

import android.content.Context
import android.content.Intent
import me.wolszon.groupie.android.ui.group.GroupActivity
import me.wolszon.groupie.android.ui.group.GroupQrActivity
import me.wolszon.groupie.android.ui.landing.LandingActivity

interface NavigatorInterface {
    fun openLandingActivity()
    fun openGroupActivity()
    fun openGroupQrActivity(groupId: String)
}

class Navigator(private val context: Context) : NavigatorInterface {
    override fun openLandingActivity() {
        context.startActivity(Intent(context, LandingActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }

    override fun openGroupActivity() {
        context.startActivity(GroupActivity.createIntent(context).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
    }

    override fun openGroupQrActivity(groupId: String) {
        context.startActivity(GroupQrActivity.createIntent(context, groupId))
    }
}