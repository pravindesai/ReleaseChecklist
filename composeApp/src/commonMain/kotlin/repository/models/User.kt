package repository.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id :String? = null,
    val name:String? = null,
    val type:String? = null,
    val adminId:String? = null,
    val fcmToken:String? = null,
)
