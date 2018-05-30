package me.wolszon.crowdie.api.domain

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import me.wolszon.crowdie.android.CrowdieApplication
import me.wolszon.crowdie.api.models.mapper.CreatedMapper
import me.wolszon.crowdie.api.models.mapper.GroupMapper
import me.wolszon.crowdie.api.models.apimodels.CreatedResponse
import me.wolszon.crowdie.api.models.apimodels.GroupResponse
import me.wolszon.crowdie.api.models.apimodels.MemberRequest
import me.wolszon.crowdie.api.models.dataclass.Group
import me.wolszon.crowdie.api.models.dataclass.Member
import me.wolszon.crowdie.api.models.mapper.MemberMapper
import me.wolszon.crowdie.api.retrofit.V1RetrofitApi
import me.wolszon.crowdie.base.Preferences
import retrofit2.HttpException

class ApiGroupManager(private val preferences: Preferences,
                      private val groupApi: V1RetrofitApi) : GroupManager {
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

    override fun getGroupObservable(): Observable<out StateFeed> = subject

    override fun newGroup(): Single<Group> {
        val creator = createMemberRequest()

        return groupApi.newGroup(creator)
                .map{ mapCreatedAndPersist(it) }
                .process()
    }

    override fun joinGroup(groupId: String): Single<Group> {
        return groupApi.findGroup(groupId)
                .map { mapJoinedAndPersist(it) }
                .process()
                .onErrorResumeNext {
                    if (it !is HttpException || it.code() !in listOf(401, 403)) {
                        throw it
                    }

                    val member = createMemberRequest()
                    return@onErrorResumeNext groupApi.addMember(groupId, member)
                            .map { mapCreatedAndPersist(it) }
                            .process()
                }
    }

    private fun mapCreatedAndPersist(createdResponse: CreatedResponse): Group {
        val group = CreatedMapper.map(createdResponse)

        createdResponse.apply {
            preferences.lastJoinedGroup = group.id
            preferences.lastJoinedGroupToken = token
            preferences.lastJoinedGroupMemberId = yourId

            GroupManager.state = GroupManager.State(group, token, yourId)
        }

        return group
    }

    private fun mapJoinedAndPersist(groupResponse: GroupResponse): Group {
        val group = GroupMapper.map(groupResponse)

        GroupManager.state = GroupManager.State(
                group,
                preferences.lastJoinedGroupToken!!,
                preferences.lastJoinedGroupMemberId!!
        )

        return group
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

    override fun updateRole(memberId: String, role: Member.Role): Single<Group> {
        if (GroupManager.state?.isAdmin() != true) {
            return Single.error { GroupManager.NoPermissionsException() }
        }

        if (memberId == GroupManager.state?.currentMemberId) {
            return Single.error { Exception("You cannot suppress yourself!") }
        }

        val mappedRole = MemberMapper.ReverseRoleMapper.map(role)

        return groupApi.updateMemberRole(memberId, mappedRole)
                .map { GroupMapper.map(it) }
                .process()
    }

    override fun kickMember(memberId: String): Single<Group> {
        if (GroupManager.state?.isAdmin() != true) {
            return Single.error { GroupManager.NoPermissionsException() }
        } else if (memberId == GroupManager.state!!.getCurrentUser().id) {
            return Single.error { Exception("You cannot kick yourself") }
        }

        return groupApi.kickMember(memberId)
                .map { GroupMapper.map(it) }
                .process()
    }

    private fun createMemberRequest(): MemberRequest =
            MemberRequest(
                    name = preferences.username,
                    lat = 0f, lng = 0f,
                    androidId = CrowdieApplication.androidId
            )

    private fun destroyState() {
        preferences.lastJoinedGroup = null
        preferences.lastJoinedGroupToken = null
        preferences.lastJoinedGroupMemberId = null

        GroupManager.state = null
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
}