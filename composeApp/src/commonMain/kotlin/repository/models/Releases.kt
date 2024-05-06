package repository.models

import kotlinx.serialization.Serializable

@Serializable
data class Releases(
    val id:String? = null,
    val adminId: String? = null,
    val version:String?=null,
    val branch:String? = null,
    val releaseDateTimeEpoch:String? = null,
    val user: User? = null,
    val project: Project? = null,
    val tag:String? = null,
    val baseCampIDCheck:Boolean = false,
    val baseCampLogCheck: Boolean = false,
)

