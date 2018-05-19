package me.wolszon.crowdie.api.models.mapper

import me.wolszon.crowdie.api.models.apimodels.MemberResponse
import me.wolszon.crowdie.api.models.dataclass.Member

class MemberMapper {
    companion object : Mapper<MemberResponse, Member> {
        override fun map(value: MemberResponse): Member = Member(
                id = value.id,
                name = value.name,
                role = value.role,
                lat = value.coordsBit.lat,
                lng = value.coordsBit.lng
        )
    }
}