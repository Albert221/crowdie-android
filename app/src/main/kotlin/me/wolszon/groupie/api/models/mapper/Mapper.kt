package me.wolszon.groupie.api.models.mapper

interface Mapper <in T, out Y> {
    fun map(value : T) : Y
}