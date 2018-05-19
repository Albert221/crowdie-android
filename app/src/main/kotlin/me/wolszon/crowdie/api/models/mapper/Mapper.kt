package me.wolszon.crowdie.api.models.mapper

interface Mapper <in T, out Y> {
    fun map(value : T) : Y
}