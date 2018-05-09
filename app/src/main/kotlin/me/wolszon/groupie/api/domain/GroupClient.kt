package me.wolszon.groupie.api.domain

import io.reactivex.Observable
import io.reactivex.Single
import me.wolszon.groupie.api.models.dataclass.Group

interface GroupClient {
    fun newGroup(): Single<Group>
    fun joinGroup(groupId: String): Single<Group>
    fun sendCoords(lat: Float, lng: Float): Single<Group>
    fun update(): Single<Group>
    fun leaveGroup(): Single<Group>

    /**
     * @return a observable to which given entities could subscribe to three events, success, error
     *         and complete:
     *
     *         - Success is when new data is being retrieved and it contains fresh group.
     *         - Error is when there's something wrong with the user, most likely one was
     *           kicked from the group.
     *         - Complete is when user decided to leave group.
     */
    fun getGroupObservable(): Observable<out StateFeed>
}