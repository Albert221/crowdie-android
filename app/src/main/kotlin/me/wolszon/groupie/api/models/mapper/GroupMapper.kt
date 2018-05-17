package me.wolszon.groupie.api.models.mapper

import me.wolszon.groupie.api.models.apimodels.GroupResponse
import me.wolszon.groupie.api.models.dataclass.Group

class GroupMapper {
    companion object : Mapper<GroupResponse, Group> {
        override fun map(value: GroupResponse): Group =
            Group(
                    id = value.id,
                    members = value.members.map { MemberMapper.map(it) }.toMutableList()
            )
    }
}