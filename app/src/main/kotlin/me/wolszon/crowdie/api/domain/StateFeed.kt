package me.wolszon.crowdie.api.domain

import me.wolszon.crowdie.api.models.dataclass.Group

class StateFeed(val event: Event, val updatedGroup: Group? = null) {
    enum class Event {
        UPDATE, KICK, LEAVE
    }

    companion object {
        fun update(updatedGroup: Group): StateFeed =
                StateFeed(Event.UPDATE, updatedGroup = updatedGroup)

        fun kick(): StateFeed = StateFeed(Event.KICK)

        fun leave(): StateFeed = StateFeed(Event.LEAVE)
    }
}