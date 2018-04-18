package me.wolszon.groupie.android.ui.landing

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.android.ui.Navigator
import me.wolszon.groupie.api.GroupManager

@Module
class LandingModule {
    @Provides
    fun provideNavigator(landingActivity: LandingActivity): Navigator = Navigator(landingActivity)

    @Provides
    fun providePresenter(groupManager: GroupManager, navigator: Navigator) =
            LandingPresenter(groupManager, navigator)
}