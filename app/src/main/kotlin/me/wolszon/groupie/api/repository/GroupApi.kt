package me.wolszon.groupie.api.repository

import io.reactivex.Single
import me.wolszon.groupie.api.models.dataclass.Group

interface GroupApi {
    fun find(id : String) : Single<Group>
}

// Example response

//{
//    "id": "d175a80a-399a-4c89-b05a-1b8e2decab57",
//    "members": [
//    {
//        "id": "4f8b6c3c-a1d0-44a7-9020-2ef3b87e6fc7",
//        "name": "Albert",
//        "role": 1,
//        "coords": {
//        "lat": 212.123123,
//        "lng": 33.345343
//    }
//    },
//    {
//        "id": "eeb30046-6275-4534-ad60-427726cbaeb7",
//        "name": "Jan",
//        "role": 0,
//        "coords": {
//        "lat": 212.435323,
//        "lng": 33.534345
//    }
//    },
//    {
//        "id": "dd8f54e9-cff4-4035-955f-4d16e9a6a714",
//        "name": "Kuba",
//        "role": 0,
//        "coords": {
//        "lat": 212.234234,
//        "lng": 21.234233
//    }
//    }
//    ]
//}