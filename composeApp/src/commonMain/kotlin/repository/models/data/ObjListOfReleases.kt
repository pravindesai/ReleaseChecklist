package repository.models.data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import repository.models.data.ObjDocument

@Serializable
data class ObjListOfReleases(

	@SerialName("documents")
	val documents: List<ObjDocument?>? = null
)
