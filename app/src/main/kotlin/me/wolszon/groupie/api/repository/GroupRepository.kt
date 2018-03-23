package me.wolszon.groupie.api.repository

import io.reactivex.Single
import me.wolszon.groupie.api.mapper.GroupMapper
import me.wolszon.groupie.api.models.dataclass.Group
import retrofit2.Retrofit

class GroupRepository(val retrofit: Retrofit) : GroupApi {
    private val groupApi by lazy { retrofit.create(GroupRetrofitApi::class.java) }

    override fun find(id: String): Single<Group> =
        groupApi.findGroup(id).map { GroupMapper.map(it) }
}