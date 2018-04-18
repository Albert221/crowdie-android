package me.wolszon.groupie.api

import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import me.wolszon.groupie.android.GroupieApplication
import me.wolszon.groupie.android.Preferences
import me.wolszon.groupie.api.models.apimodels.MemberRequest
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.api.repository.GroupApi
import me.wolszon.groupie.base.Schedulers

interface GroupClient : ObservableSource<Group> {
    fun newGroup()
    fun joinGroup(groupId: String)
    fun sendCoords(lat: Float, lng: Float)
    fun update()
    fun leaveGroup()
}

interface GroupAdmin : ObservableSource<Group> {
    fun updateRole(memberId: String, role: Int)
    fun kickMember(memberId: String)

    class NoPermissionsException : Exception()
}

interface GroupManager : GroupClient, GroupAdmin

class ApiGroupManager(private val preferences: Preferences,
                      private val groupApi: GroupApi,
                      private val schedulers: Schedulers) : GroupManager {
    var state: State? = null
        private set
    private val subject: BehaviorSubject<Group> = BehaviorSubject.create()
    
    init {
        subject.subscribe {
            state = State(it)
        }
    }

    override fun newGroup() {
        val creator = createMemberRequest()

        groupApi.newGroup(creator)
                .process()
    }

    override fun joinGroup(groupId: String) {
        val member = createMemberRequest()

        groupApi.addMember(groupId, member)
                .process()
    }

    override fun sendCoords(lat: Float, lng: Float) {
        groupApi.sendMemberCoordsBit(state!!.currentUser.id, lat, lng)
                .process()
    }

    override fun update() {
        groupApi.find(state!!.groupId)
                .process()
    }

    override fun leaveGroup() {
        groupApi.kickMember(state!!.currentUser.id)
        state = null
    }

    override fun updateRole(memberId: String, role: Int) {
        throwExceptionWhenNotAnAdmin()

        groupApi.updateMemberRole(memberId, role)
                .process()
    }

    override fun kickMember(memberId: String) {
        throwExceptionWhenNotAnAdmin()

        if (memberId == state!!.currentUser.id) {
            throw Exception("You cannot kick yourself")
        }

        groupApi.kickMember(memberId)
                .process()
    }

    override fun subscribe(observer: Observer<in Group>) {
        subject.subscribe(observer)
    }

    private fun createMemberRequest(): MemberRequest =
            MemberRequest(
                    name = preferences.username,
                    lat = 0f, lng = 0f,
                    androidId = GroupieApplication.androidId
            )

    private fun throwExceptionWhenNotAnAdmin() {
        if (state!!.currentUser.role != Member.ADMIN) {
            throw GroupAdmin.NoPermissionsException()
        }
    }

    private fun Single<Group>.process() {
        this
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({ subject.onNext(it) }, { subject.onError(it) })
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