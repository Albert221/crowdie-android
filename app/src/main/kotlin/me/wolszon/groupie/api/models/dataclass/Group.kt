package me.wolszon.groupie.api.models.dataclass

data class Group (
        val id: String,
        val members: MutableList<Member>
)