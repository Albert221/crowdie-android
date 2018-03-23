package me.wolszon.groupie.api.mapper

interface Mapper <in T, out Y> {
    fun map(value : T) : Y
}