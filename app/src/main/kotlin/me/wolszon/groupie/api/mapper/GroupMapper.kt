package me.wolszon.groupie.api.mapper

import me.wolszon.groupie.api.models.apimodels.GroupResponse
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member

class GroupMapper {
    companion object : Mapper<GroupResponse, Group> {
        override fun map(value: GroupResponse): Group =
            Group(value.members.map { MemberMapper.map(it) }.toMutableList())
    }
}