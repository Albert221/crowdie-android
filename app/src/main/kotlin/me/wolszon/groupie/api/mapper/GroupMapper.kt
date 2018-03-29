package me.wolszon.groupie.api.mapper

import me.wolszon.groupie.api.models.apimodels.GroupResponse
import me.wolszon.groupie.api.models.dataclass.Group
import me.wolszon.groupie.api.models.dataclass.Member

class GroupMapper {
    companion object : Mapper<GroupResponse, Group> {
        override fun map(value: GroupResponse): Group =
            Group(value.members.map { Member(it.name, it.coordsBits[0].lat, it.coordsBits[0].lng) })
    }
}