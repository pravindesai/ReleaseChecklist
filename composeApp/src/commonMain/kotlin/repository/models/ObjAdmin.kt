package repository.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ObjAdmin(

	@SerialName("createTime")
	val createTime: String? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("updateTime")
	val updateTime: String? = null,

	@SerialName("fields")
	val fields: Fields? = null
)

@Serializable
data class Adminpass(

	@SerialName("stringValue")
	val stringValue: String? = null
)

@Serializable
data class Adminid(

	@SerialName("stringValue")
	val stringValue: String? = null
)

@Serializable
data class Fields(

	@SerialName("org")
	val org: Org? = null,

	@SerialName("adminid")
	val adminid: Adminid? = null,

	@SerialName("adminpass")
	val adminpass: Adminpass? = null
)

@Serializable
data class Org(

	@SerialName("stringValue")
	val stringValue: String? = null
)
