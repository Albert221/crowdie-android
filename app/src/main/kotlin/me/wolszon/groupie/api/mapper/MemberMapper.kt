package me.wolszon.groupie.api.mapper

import me.wolszon.groupie.api.models.apimodels.MemberResponse
import me.wolszon.groupie.api.models.dataclass.Member
import me.wolszon.groupie.api.state.GroupState.currentUser

class MemberMapper {
    companion object : Mapper<MemberResponse, Member> {
        override fun map(value: MemberResponse): Member {
            val member = Member(
                    id = value.id,
                    name = value.name,
                    role = value.role,
                    lat = value.coordsBit.lat,
                    lng = value.coordsBit.lng,
                    androidId = value.androidId
            )

            // FIXME: Mapped maybe isn't the best place for that logic,
            // FIXME: but it's the best for now.
            if (member.isYou()) {
                currentUser = member
            }

            return member
        }
    }
}