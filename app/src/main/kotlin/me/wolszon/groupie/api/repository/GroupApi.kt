package me.wolszon.groupie.api.repository

import io.reactivex.Single
import me.wolszon.groupie.api.models.dataclass.Group

interface GroupApi {
    fun find(id : String) : Single<Group>
}