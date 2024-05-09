package repository.models.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ObjListOfUsers(

	@SerialName("ObjListOfUsers")
	val objListOfUsers: List<ObjListOfUsersItem?>? = null
)

@Serializable
data class ObjListOfUsersItem(

	@SerialName("document")
	val document: ObjUser? = null,

	@SerialName("readTime")
	val readTime: String? = null
)
