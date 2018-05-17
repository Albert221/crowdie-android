package me.wolszon.groupie.android.ui.landing

import dagger.Module
import dagger.Provides
import me.wolszon.groupie.android.ui.Navigator
import me.wolszon.groupie.api.domain.GroupManager
import me.wolszon.groupie.base.Preferences
import me.wolszon.groupie.base.Schedulers

@Module
class LandingModule {
    @Provides
    fun provideNavigator(landingActivity: LandingActivity): Navigator = Navigator(landingActivity)

    @Provides
    fun providePresenter(groupManager: GroupManager, navigator: Navigator, schedulers: Schedulers,
                         preferences: Preferences) =
            LandingPresenter(groupManager, navigator, schedulers, preferences)
}