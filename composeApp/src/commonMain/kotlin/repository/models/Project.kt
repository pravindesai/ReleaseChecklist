package repository.models

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id:String? = null,
    val name:String? = null,
    val adminId:String?  = null,
)