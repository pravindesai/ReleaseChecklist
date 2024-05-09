package repository.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BooleanValue(
    @SerialName("booleanValue")
    val booleanValue: Boolean? = null
)