package me.wolszon.groupie.api.models.dataclass

data class Member (
        val id: String,
        val name: String,
        val role: Int,
        val lat: Float,
        val lng: Float
) {
    companion object {
        const val MEMBER = 0
        const val ADMIN = 1
    }
}