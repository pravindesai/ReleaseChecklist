package repository.models.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import repository.models.Fields


@Serializable
data class ObjDocument(

    @SerialName("createTime")
    val createTime: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("updateTime")
    val updateTime: String? = null,

    @SerialName("fields")
    val fields: Fields? = null,

    @Transient
    var isExpanded: Boolean = true
)

