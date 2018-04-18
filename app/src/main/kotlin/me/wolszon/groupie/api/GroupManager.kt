package me.wolszon.groupie.api

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import me.wolszon.groupie.android.GroupieApplication
import me.wolszon.groupie.android.Preferences
import me.wolszon.groupie.api.models.apimodels.MemberRequest
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.api.repository.GroupApi

interface GroupClient {
    fun newGroup(): Single<Group>
    fun joinGroup(groupId: String): Single<Group>
    fun sendCoords(lat: Float, lng: Float): Single<Group>
    fun update(): Single<Group>
    fun leaveGroup(): Single<Group>

    fun getObservable(): Observable<out Group>
}

interface GroupAdmin {
    fun updateRole(memberId: String, role: Int): Single<Group>
    fun kickMember(memberId: String): Single<Group>

    fun getObservable(): Observable<out Group>

    class NoPermissionsException : Exception()
}

interface GroupManager : GroupClient, GroupAdmin

class ApiGroupManager(private val preferences: Preferences,
                      private val groupApi: GroupApi) : GroupManager {
    var state: State? = null
        private set
    private val subject: BehaviorSubject<Group> = BehaviorSubject.create()
    
    init {
        subject.subscribe {
            state = State(it)
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
        throwExceptionWhenNotAnAdmin()

        return groupApi.updateMemberRole(memberId, role)
                .doOnSuccess(subject::onNext)
    }

    override fun kickMember(memberId: String): Single<Group> {
        throwExceptionWhenNotAnAdmin()

        if (memberId == state!!.currentUser.id) {
//            throw Exception("You cannot kick yourself")
        }

        return groupApi.kickMember(memberId)
                .doOnSuccess(subject::onNext)
    }

    override fun getObservable(): Observable<out Group> = subject

    private fun createMemberRequest(): MemberRequest =
            MemberRequest(
                    name = preferences.username,
                    lat = 0f, lng = 0f,
                    androidId = GroupieApplication.androidId
            )

    private fun throwExceptionWhenNotAnAdmin() {
        if (state!!.currentUser.role != Member.ADMIN) {
//            throw GroupAdmin.NoPermissionsException()
        }
    }

    class State(
            val group: Group
    ) {
        val groupId: String
            get() = group.id
        val currentUser
            get() = group.members.find { it.isYou() }!!
    }
}