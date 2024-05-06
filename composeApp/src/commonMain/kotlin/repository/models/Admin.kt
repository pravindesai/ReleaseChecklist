package repository.models

import kotlinx.serialization.Serializable

@Serializable
data class Admin(
    val id:String? = null,
    val name:String?  = null,
    val fcmToken:String? = null
)