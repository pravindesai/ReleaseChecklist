package repository.models.data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ObjListOfProject(

	@SerialName("documents")
	val documents: List<ObjDocument?>? = null
)

