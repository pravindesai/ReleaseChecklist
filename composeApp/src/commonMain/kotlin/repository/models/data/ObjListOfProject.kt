package repository.models.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import repository.models.Fields

@Serializable
data class ObjListOfProject(

	@SerialName("ObjListOfProject")
	val objListOfProject: List<ObjListOfProjectItem?>? = null
)

@Serializable
data class ObjListOfProjectItem(

	@SerialName("document")
	val document: ObjDocument? = null,

	@SerialName("readTime")
	val readTime: String? = null
)