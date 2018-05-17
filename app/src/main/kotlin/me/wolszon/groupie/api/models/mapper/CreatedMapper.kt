package me.wolszon.groupie.api.models.mapper

import me.wolszon.groupie.api.models.apimodels.CreatedResponse
import me.wolszon.groupie.api.models.dataclass.Group

class CreatedMapper {
    companion object : Mapper<CreatedResponse, Group> {
        override fun map(value: CreatedResponse): Group =
                GroupMapper.map(value.group)
    }
}