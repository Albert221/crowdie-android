package me.wolszon.groupie.android.ui.group

import io.reactivex.Observable
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.base.BasePresenter
import me.wolszon.groupie.base.Schedulers
import me.wolszon.groupie.android.ui.Navigator
import me.wolszon.groupie.api.state.GroupState
import java.util.concurrent.TimeUnit

class GroupPresenter(private val schedulers: Schedulers,
                     private val groupApi: GroupApi,
                     val navigator: Navigator) : BasePresenter<GroupView>() {
    private val groupId: String by lazy { GroupState.groupId!! }
    private lateinit var group: Group

    override fun subscribe(view: GroupView) {
        super.subscribe(view)

        subscribeToGroupUpdates()
    }

    private fun subscribeToGroupUpdates() {
        run {
            Observable.interval(3, 2, TimeUnit.SECONDS)
                    .subscribe { loadMembers() }
        }
    }

    // This method is called so frequently, because during
    fun loadMembers() {
        run {
            groupApi.find(groupId)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({
                        group = it
                        view?.showMembers(it.members)
                    }, { view?.showErrorDialog(it) })
        }
    }

    fun promoteMember(id: String) {
        updateMemberRole(id, Member.ADMIN)
    }

    fun suppressMember(id: String) {
        updateMemberRole(id, Member.MEMBER)
    }

    private fun updateMemberRole(id: String, role: Int) {
        run {
            groupApi.updateMemberRole(id, role)
                    .subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({
                        loadMembers()
                    }, { view?.showErrorDialog(it) })
        }
    }

    fun blockMember(id: String) {
        val member = group.members.find { it.id == id }!!

        view?.displayMemberBlockConfirmation(member) {
            result ->
            if (!result) return@displayMemberBlockConfirmation

            run {
                groupApi.kickMember(id)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            loadMembers()
                        }, { view?.showErrorDialog(it) })
            }
        }
    }

    fun showQr() {
        navigator.openGroupQrActivity(groupId)
    }
}