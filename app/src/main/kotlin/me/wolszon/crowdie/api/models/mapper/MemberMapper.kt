package me.wolszon.crowdie.api.models.mapper

import me.wolszon.crowdie.api.models.apimodels.MemberResponse
import me.wolszon.crowdie.api.models.dataclass.Member

object MemberMapper : Mapper<MemberResponse, Member> {
    override fun map(value: MemberResponse): Member = Member(
            id = value.id,
            name = value.name,
            role = RoleMapper.map(value.role),
            lat = value.coordsBit.lat,
            lng = value.coordsBit.lng
    )

    object RoleMapper : Mapper<Int, Member.Role> {
        override fun map(value: Int): Member.Role = when (value) {
            0 -> Member.Role.MEMBER
            1 -> Member.Role.ADMIN
            else -> throw Exception("Invalid role '%d' specified.".format(value))
        }
    }

    object ReverseRoleMapper : Mapper<Member.Role, Int> {
        override fun map(value: Member.Role): Int = when (value) {
            Member.Role.MEMBER -> 0
            Member.Role.ADMIN -> 1
        }
    }
}