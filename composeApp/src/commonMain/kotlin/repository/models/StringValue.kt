package repository.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StringValue(

    @SerialName("stringValue")
    val stringValue: String? = null
)
