package me.wolszon.crowdie.api.models.mapper

import me.wolszon.crowdie.api.models.apimodels.CreatedResponse
import me.wolszon.crowdie.api.models.dataclass.Group

class CreatedMapper {
    companion object : Mapper<CreatedResponse, Group> {
        override fun map(value: CreatedResponse): Group =
                GroupMapper.map(value.group)
    }
}