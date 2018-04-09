package me.wolszon.groupie.ui.landing

import me.wolszon.groupie.GroupieApplication
import me.wolszon.groupie.api.models.apimodels.MemberRequest
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.base.Schedulers
import me.wolszon.groupie.ui.Navigator

class LandingPresenter(private val schedulers: Schedulers,
                       private val groupApi: GroupApi,
                       private val navigator: Navigator) : BasePresenter<LandingView>() {
    fun createGroup() {
        run {
            val creator = getUserMemberRequest()

            groupApi.newGroup(creator)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({
                        navigator.openGroupActivity(it.id)
                    }, { view?.showErrorDialog(it) })
        }
    }

    fun joinExistingGroup(groupId: String) {
        run {
            val member = getUserMemberRequest()

            groupApi.addMember(groupId, member)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({
                        navigator.openGroupActivity(groupId)
                    }, { view?.showErrorDialog(it) })
        }
    }

    // FIXME: Hardcoded data
    private fun getUserMemberRequest(): MemberRequest =
            MemberRequest(name = "John Doe", lat = 54.446838f, lng = 18.571800f, androidId = GroupieApplication.androidId)
}