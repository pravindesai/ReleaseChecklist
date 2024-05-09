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
    val fcmToken: StringValue? = null,

    @SerialName("projectName")
    val projectName: StringValue? = null,

    @SerialName("projectId")
    val projectId: StringValue? = null,

    @SerialName("baseCampLogCheked")
    val baseCampLogCheked: BooleanValue? = null,

    @SerialName("releaseId")
    val releaseId: StringValue? = null,

    @SerialName("branchName")
    val branchName: StringValue? = null,

    @SerialName("tag")
    val tag: StringValue? = null,

    @SerialName("userId")
    val userId: StringValue? = null,

    @SerialName("baseCampIDChecked")
    val baseCampIDChecked: BooleanValue? = null,

    @SerialName("releaseDateTimeEpoch")
    val releaseDateTimeEpoch: StringValue? = null


)