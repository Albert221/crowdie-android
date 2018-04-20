package me.wolszon.groupie.api.domain

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import me.wolszon.groupie.android.GroupieApplication
import me.wolszon.groupie.api.models.apimodels.MemberRequest
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.api.repository.GroupApi

class ApiGroupManager(private val preferences: Preferences,
                      private val groupApi: GroupApi) : GroupManager {
    private var state: GroupManager.State? = null
    private val subject: BehaviorSubject<Group> = BehaviorSubject.create()
    
    init {
        subject.subscribe {
            state = GroupManager.State(it)
        }
    }

    override fun newGroup(): Single<Group> {
        val creator = createMemberRequest()

        return groupApi.newGroup(creator)
                .doOnSuccess(subject::onNext)
    }

    override fun joinGroup(groupId: String): Single<Group> {
        val member = createMemberRequest()

        return groupApi.addMember(groupId, member)
                .doOnSuccess(subject::onNext)
    }

    override fun sendCoords(lat: Float, lng: Float): Single<Group> {
        return groupApi.sendMemberCoordsBit(state!!.currentUser.id, lat, lng)
                .doOnSuccess(subject::onNext)
    }

    override fun update(): Single<Group> {
        return groupApi.find(state!!.groupId)
                .doOnSuccess(subject::onNext)
    }

    override fun leaveGroup(): Single<Group> {
        return groupApi.kickMember(state!!.currentUser.id)
                .doOnSuccess {
                    subject.onComplete()
                    state = null
                }
    }

    override fun updateRole(memberId: String, role: Int): Single<Group> {
        if (!isAdmin()) {
            return Single.error { GroupAdmin.NoPermissionsException() }
        }

        return groupApi.updateMemberRole(memberId, role)
                .doOnSuccess(subject::onNext)
    }

    override fun kickMember(memberId: String): Single<Group> {
        if (!isAdmin()) {
            return Single.error { GroupAdmin.NoPermissionsException() }
        } else if (memberId == state!!.currentUser.id) {
            return Single.error { Exception("You cannot kick yourself") }
        }

        return groupApi.kickMember(memberId)
                .doOnSuccess(subject::onNext)
    }

    override fun getGroupObservable(): Observable<out Group> = subject

    override fun getState(): GroupManager.State? = state?.copy()

    private fun createMemberRequest(): MemberRequest =
            MemberRequest(
                    name = preferences.username,
                    lat = 0f, lng = 0f,
                    androidId = GroupieApplication.androidId
            )

    private fun isAdmin() = state?.currentUser?.role == Member.ADMIN
}