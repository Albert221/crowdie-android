package me.wolszon.groupie.api.domain

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import me.wolszon.groupie.android.GroupieApplication
import me.wolszon.groupie.api.mapper.CreatedMapper
import me.wolszon.groupie.api.mapper.GroupMapper
import me.wolszon.groupie.api.models.apimodels.CreatedResponse
import me.wolszon.groupie.api.models.apimodels.GroupResponse
import me.wolszon.groupie.api.models.apimodels.MemberRequest
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.repository.GroupRetrofitApi
import retrofit2.HttpException
import retrofit2.Retrofit

class ApiGroupManager(private val preferences: Preferences,
                      private val retrofit: Retrofit) : GroupManager {
    private val groupApi by lazy { retrofit.create(GroupRetrofitApi::class.java) }
    private val subject: BehaviorSubject<StateFeed> = BehaviorSubject.create()

    init {
        subject.subscribe({
            if (it.event == StateFeed.Event.UPDATE) {
                GroupManager.state!!.group = it.updatedGroup!!
            } else if (it.event in listOf(StateFeed.Event.KICK,
                                          StateFeed.Event.LEAVE)) {
                destroyState()
            }
        }, { destroyState() })
    }

    override fun newGroup(): Single<Group> {
        val creator = createMemberRequest()

        return groupApi.newGroup(creator)
                .map(createdPersistingMapper)
                .process()
    }

    override fun joinGroup(groupId: String): Single<Group> {
        return groupApi.findGroup(groupId)
                .map(joinedPersistingMapper)
                .process()
                .onErrorResumeNext {
                    if (it !is HttpException) {
                        throw it
                    }

                    val member = createMemberRequest()
                    return@onErrorResumeNext groupApi.addMember(groupId, member)
                            .map(createdPersistingMapper)
                            .process()
                }
    }

    private val joinedPersistingMapper: (GroupResponse) -> Group = {
        val group = GroupMapper.map(it)

        GroupManager.state = GroupManager.State(
                group,
                preferences.lastJoinedGroupToken!!,
                preferences.lastJoinedGroupMemberId!!
        )

        group
    }

    private val createdPersistingMapper: (CreatedResponse) -> Group = {
        val group = CreatedMapper.map(it)

        preferences.lastJoinedGroup = group.id
        preferences.lastJoinedGroupToken = it.token
        preferences.lastJoinedGroupMemberId = it.yourId

        GroupManager.state = GroupManager.State(group, it.token, it.yourId)

        group
    }

    override fun sendCoords(lat: Float, lng: Float): Single<Group> {
        if (GroupManager.state == null) {
            return Single.never()
        }

        return groupApi.sendMemberCoordsBit(GroupManager.state!!.getCurrentUser().id, lat, lng)
                .map { GroupMapper.map(it) }
                .process()
    }

    override fun update(): Single<Group> {
        if (GroupManager.state == null) {
            return Single.never()
        }

        return groupApi.findGroup(GroupManager.state!!.getGroupId())
                .map { GroupMapper.map(it) }
                .process()
    }

    override fun leaveGroup(): Single<Group> {
        return groupApi.kickMember(GroupManager.state!!.getCurrentUser().id)
                .map { GroupMapper.map(it) }
                .doOnSuccess { subject.onNext(StateFeed.leave()) }
    }

    override fun updateRole(memberId: String, role: Int): Single<Group> {
        if (GroupManager.state?.isAdmin() != true) {
            return Single.error { GroupAdmin.NoPermissionsException() }
        }

        return groupApi.updateMemberRole(memberId, role)
                .map { GroupMapper.map(it) }
                .process()
    }

    override fun kickMember(memberId: String): Single<Group> {
        if (GroupManager.state?.isAdmin() != true) {
            return Single.error { GroupAdmin.NoPermissionsException() }
        } else if (memberId == GroupManager.state!!.getCurrentUser().id) {
            return Single.error { Exception("You cannot kick yourself") }
        }

        return groupApi.kickMember(memberId)
                .map { GroupMapper.map(it) }
                .process()
    }

    override fun getGroupObservable(): Observable<out StateFeed> = subject

    private fun createMemberRequest(): MemberRequest =
            MemberRequest(
                    name = preferences.username,
                    lat = 0f, lng = 0f,
                    androidId = GroupieApplication.androidId
            )

    private fun destroyState() {
        preferences.lastJoinedGroup = null
        preferences.lastJoinedGroupToken = null
        preferences.lastJoinedGroupMemberId = null

        GroupManager.state = null
    }

    private fun Single<Group>.notFoundAsKicked(): Single<Group> {
        return this
                .doOnError {
                    if (it is HttpException && it.code() == 404) {
                        subject.onNext(
                                StateFeed.kick()
                        )
                    }
                }
    }

    private fun Single<Group>.process(): Single<Group> {
        return this
                .notFoundAsKicked()
                .doOnSuccess {
                    if (it.members.find { it.isYou() } == null) {
                        // User is not present in members list, he's kicked.
                        subject.onNext(
                                StateFeed.kick()
                        )
                    } else {
                        subject.onNext(
                                StateFeed.update(it)
                        )
                    }
                }
    }
}