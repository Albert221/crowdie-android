package me.wolszon.groupie.api.mapper

import me.wolszon.groupie.api.models.apimodels.MemberResponse
import me.wolszon.groupie.api.models.dataclass.Member

class MemberMapper {
    companion object : Mapper<MemberResponse, Member> {
        override fun map(value: MemberResponse): Member =
                Member(value.id, value.name, value.role, value.coordsBit.lat, value.coordsBit.lng)
    }
}