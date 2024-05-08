package repository.models.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import repository.models.data.ObjAdmin


@Serializable
data class ObjListOfAdmin(

	@SerialName("documents")
	val objAdmins: List<ObjAdmin?>? = null
)
