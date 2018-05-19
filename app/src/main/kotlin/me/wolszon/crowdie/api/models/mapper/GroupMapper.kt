package me.wolszon.crowdie.api.models.mapper

import me.wolszon.crowdie.api.models.apimodels.GroupResponse
import me.wolszon.crowdie.api.models.dataclass.Group

class GroupMapper {
    companion object : Mapper<GroupResponse, Group> {
        override fun map(value: GroupResponse): Group =
            Group(
                    id = value.id,
                    members = value.members.map { MemberMapper.map(it) }.toMutableList()
            )
    }
}