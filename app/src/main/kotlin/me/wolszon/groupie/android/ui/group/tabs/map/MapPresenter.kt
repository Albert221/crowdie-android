package me.wolszon.groupie.android.ui.group.tabs.map

import me.wolszon.groupie.api.domain.GroupManager
import me.wolszon.groupie.api.domain.StateFeed
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.base.Schedulers

class MapPresenter(private val groupManager: GroupManager,
                   private val schedulers: Schedulers) : BasePresenter<MapView>() {
    override fun subscribe(view: MapView) {
        super.subscribe(view)

        run {
            groupManager
                    .getGroupObservable()
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe {
                        when (it.event) {
                            StateFeed.Event.UPDATE -> {
                                handleUpdate(it.updatedGroup!!)
                            }

                            else -> Unit
                        }
                    }
        }
    }

    private fun handleUpdate(group: Group) {
        view?.showMembers(group.members)
    }
}