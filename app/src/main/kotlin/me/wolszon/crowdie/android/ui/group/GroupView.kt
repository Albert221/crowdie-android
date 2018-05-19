package me.wolszon.crowdie.android.ui.group

import me.wolszon.crowdie.base.BaseView

interface GroupView : BaseView {
    fun setSettingsVisibility(visible: Boolean)
    fun informAboutBeingKicked()
}