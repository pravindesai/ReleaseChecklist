package repository.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Fields(

    @SerialName("org")
    val org: StringValue? = null,

    @SerialName("adminid")
    val adminid: StringValue? = null,

    @SerialName("adminpass")
    val adminpass: StringValue? = null,

    @SerialName("userpass")
    val userpass: StringValue? = null,

    @SerialName("adminId")
    val adminId: StringValue? = null,

    @SerialName("userid")
    val userid: StringValue? = null,

    @SerialName("fcmToken")
    val fcmToken: StringValue? = null

)