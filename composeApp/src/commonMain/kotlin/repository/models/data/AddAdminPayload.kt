package repository.models.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import repository.models.Fields

@Serializable
data class AddAdminPayload(

	@SerialName("fields")
	val fields: Fields? = null
)
