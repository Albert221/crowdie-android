package me.wolszon.crowdie.android.ui.group.tabs.qr

import dagger.Module
import dagger.Provides

@Module
class QrTabModule {
    @Provides
    fun provideQrPresenter() = QrPresenter()
}