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

    fun getGroupObservable(): Observable<out Group>
}