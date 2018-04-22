package me.wolszon.groupie.api.domain

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import me.wolszon.groupie.android.GroupieApplication
import me.wolszon.groupie.api.models.apimodels.MemberRequest
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.repository.GroupApi
import retrofit2.HttpException

class ApiGroupManager(private val preferences: Preferences,
                      private val groupApi: GroupApi) : GroupManager {
    private var state: GroupManager.State? = null
    private val subject: BehaviorSubject<Group> = BehaviorSubject.create()

    init {
        subject.subscribe({
            preferences.lastJoinedGroup = it.id
            state = GroupManager.State(it)
        }, { destroyState() }, { destroyState() })
    }

    override fun newGroup(): Single<Group> {
        val creator = createMemberRequest()

        return groupApi.newGroup(creator)
                .informSubject()
    }

    override fun joinGroup(groupId: String): Single<Group> {
        val member = createMemberRequest()

        return groupApi.addMember(groupId, member)
                .informSubject()
    }

    override fun sendCoords(lat: Float, lng: Float): Single<Group> {
        if (state == null) {
            return Single.never()
        }

        return groupApi.sendMemberCoordsBit(state!!.currentUser.id, lat, lng)
                .informSubject()
    }

    override fun update(): Single<Group> {
        return groupApi.find(state!!.groupId)
                .informSubject()
    }

    override fun leaveGroup(): Single<Group> {
        return groupApi.kickMember(state!!.currentUser.id)
                .doOnSuccess { subject.onComplete() }
    }

    override fun updateRole(memberId: String, role: Int): Single<Group> {
        if (state?.isAdmin() != true) {
            return Single.error { GroupAdmin.NoPermissionsException() }
        }

        return groupApi.updateMemberRole(memberId, role)
                .informSubject()
    }

    override fun kickMember(memberId: String): Single<Group> {
        if (state?.isAdmin() != true) {
            return Single.error { GroupAdmin.NoPermissionsException() }
        } else if (memberId == state!!.currentUser.id) {
            return Single.error { Exception("You cannot kick yourself") }
        }

        return groupApi.kickMember(memberId)
                .informSubject()
    }

    override fun getGroupObservable(): Observable<out Group> = subject

    override fun getState(): GroupManager.State? = state?.copy()

    private fun createMemberRequest(): MemberRequest =
            MemberRequest(
                    name = preferences.username,
                    lat = 0f, lng = 0f,
                    androidId = GroupieApplication.androidId
            )

    private fun destroyState() {
        preferences.lastJoinedGroup = null
        state = null
    }

    private fun Single<Group>.informSubject(): Single<Group> {
        return this
                .doOnSuccess {
                    if (it.members.find { it.isYou() } == null) {
                        // User is not present in members list, he's kicked.
                        subject.onError(GroupClient.Kicked())
                    } else {
                        subject.onNext(it)
                    }
                }
                .doOnError {
                    Log.d("test", "dupa", it)
                    if (it is HttpException && it.code() == 404) {
                        subject.onError(GroupClient.Kicked())
                    }
                }
    }
}