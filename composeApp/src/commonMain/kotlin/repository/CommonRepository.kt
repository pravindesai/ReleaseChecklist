package repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import repository.models.ApiResult
import repository.models.Field
import repository.models.FieldFilter
import repository.models.Fields
import repository.models.FromItem
import repository.models.Query
import repository.models.StringValue
import repository.models.StructuredQuery
import repository.models.Value
import repository.models.Where
import repository.models.data.AddAdminPayload
import repository.models.data.IntUser
import repository.models.data.ObjAdmin
import repository.models.data.ObjDocument
import repository.models.data.ObjListOfProjectItem
import repository.models.data.ObjListOfReleases
import repository.models.data.ObjListOfUsersItem
import repository.models.data.ObjUser
import strings.UserType
import strings.encrypt
import kotlin.time.Duration.Companion.seconds

object CommonRepository {
    private var currentAdmin: ObjAdmin? = null
    private var currentUser: ObjUser? = null
    private var currentUserAdminId: String? = null

    const val NO_DATA_FOUND_PLACEHOLDER = "https://firebasestorage.googleapis.com/v0/b/releasechecklist-kmm-app.appspot.com/o/no_data_found.jpg?alt=media&token=dfe8e268-232c-414e-904e-98080dcfdf3e"
    const val BASE_URL = "firestore.googleapis.com/v1/projects/releasechecklist-kmm-app/databases/(default)/documents"
    const val HTTPS_BASE_URL = "https://$BASE_URL"
    const val ADMIN = "admin"
    const val USER = "user"
    const val PROJECT = "project"
    const val RELEASES = "releases"

    const val QUERYPATH = ":runQuery"

    fun getCurrentAdminId(): String? {
        return currentAdmin?.fields?.adminId?.stringValue
            ?: currentAdmin?.fields?.adminid?.stringValue ?:  currentUserAdminId
    }

    fun getCurrentUserId():String ? {
        return (currentUser as? ObjUser)?.fields?.userId?.stringValue
            ?: (currentUser as? ObjUser)?.fields?.userid?.stringValue
    }

    private val client = HttpClient() {
        install(Resources)
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        defaultRequest {
            host = BASE_URL
            url {
                protocol = URLProtocol.HTTPS
            }
        }
    }

    private val clientWithoutDefaults = HttpClient() {
        install(Resources)
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }


    fun getLoggedInUser() = currentAdmin?:currentUser

    fun setCurrentUser(user: IntUser?) {
        if (user==null){
            currentAdmin = null
            currentUser = null
        }
        when(user){
            is ObjAdmin -> currentAdmin = user
            is ObjUser -> {
                currentUser = user
                currentUserAdminId = currentUser?.fields?.adminId?.stringValue?:currentUser?.fields?.adminid?.stringValue
            }
        }
    }

    suspend fun trySignIn(
        userId: String,
        userPassword: String,
        userType: UserType
    ): Pair<Boolean, IntUser?> {
        when (userType) {
            UserType.ADMIN -> {
                return try {
                    val httpResponse = client.get {
                        url {
                            appendPathSegments(ADMIN, userId)
                        }
                    }
                    logHttpResponse(httpResponse)
                    if (httpResponse.status == HttpStatusCode.OK) {
                        val adminOfMatchingId = httpResponse.body<ObjAdmin?>()
                        val isLoggedIn =
                            adminOfMatchingId?.fields?.adminid?.stringValue.equals(userId) and adminOfMatchingId?.fields?.adminpass?.stringValue.equals(
                                userPassword?.encrypt()
                            )
                        setCurrentUser(adminOfMatchingId.takeIf { isLoggedIn })

                        Pair(isLoggedIn, adminOfMatchingId)
                    } else {
                        setCurrentUser(null)
                        Pair(false, null)
                    }

                } catch (e: Exception) {
                    setCurrentUser(null)
                    println("*** " + e.message)
                    Pair(false, null)
                }
            }

            UserType.USER -> {
                return try {
                    val httpResponse = client.get {
                        url {
                            appendPathSegments(USER, userId)
                        }
                    }
                    logHttpResponse(httpResponse)
                    if (httpResponse.status == HttpStatusCode.OK) {
                        val userOfMatchingId = httpResponse.body<ObjUser?>()
                        val isLoggedIn =
                            userOfMatchingId?.fields?.userid?.stringValue.equals(userId) and userOfMatchingId?.fields?.userpass?.stringValue.equals(
                                userPassword.encrypt()
                            )
                        setCurrentUser(userOfMatchingId.takeIf { isLoggedIn })
                        Pair(isLoggedIn, userOfMatchingId)

                    } else {
                        setCurrentUser(null)
                        Pair(false, null)
                    }

                } catch (e: Exception) {
                    setCurrentUser(null)
                    println("*** " + e.message)
                    Pair(false, null)
                }
            }
        }
    }

    suspend fun signOut(): Boolean {
        delay(500)
        setCurrentUser(null)
        return true
    }

    suspend fun addUser(userId: String, userPass: String): ApiResult<ObjUser> {
        return try {

            val httpResponse = client.post(USER) {
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("documentId", userId)
                }
                setBody(
                    AddAdminPayload(
                        fields = Fields(
                            userId = StringValue(stringValue = userId),
                            userpass = StringValue(stringValue = userPass.encrypt()),
                            adminId = StringValue(stringValue = getCurrentAdminId()),
                        )
                    )
                )
            }
            logHttpResponse(httpResponse)

            if (httpResponse.status == HttpStatusCode.OK) {
                val user = httpResponse.body<ObjUser?>()
                ApiResult(
                    success = true,
                    data = user,
                    message = httpResponse.body<String>()
                )
            } else {
                ApiResult(
                    success = false,
                    data = null,
                    message = httpResponse.body<String>()
                )
            }

        } catch (e: Exception) {
            println("*** " + e.message)
            ApiResult(
                success = false,
                data = null,
                message = e.message
            )
        }
    }

    suspend fun removeUser(userId: String): Boolean {
        delay(2.seconds)
        return true
    }

    suspend fun getAllUsersForAdmin(adminId: String): ApiResult<List<ObjUser>>? {

        return try {

            val httpResponse = clientWithoutDefaults.post(HTTPS_BASE_URL.plus(QUERYPATH)) {
                contentType(ContentType.Application.Json)
                setBody(
                    Query(
                        structuredQuery = StructuredQuery(
                            from = listOf(FromItem(collectionId = USER)),
                            where = Where(
                                fieldFilter = FieldFilter(
                                    field = Field(
                                        fieldPath = "adminId"
                                    ),
                                    op = "EQUAL",
                                    value = Value(
                                        stringValue = adminId
                                    )
                                )
                            )
                        )
                    )
                )
            }
            logHttpResponse(httpResponse)

            if (httpResponse.status == HttpStatusCode.OK) {
                val listOfUsers = httpResponse.body<List<ObjListOfUsersItem>>()
                ApiResult(
                    success = true,
                    data = listOfUsers.map { it.document }.filterNotNull(),
                    message = httpResponse.body<String>()
                )
            } else {
                ApiResult(
                    success = false,
                    data = emptyList(),
                    message = httpResponse.body<String>()
                )
            }

        } catch (e: Exception) {
            println("*** " + e.message)
            ApiResult(
                success = false,
                data = emptyList(),
                message = e.message
            )
        }
    }

    suspend fun getAllProjectForAdmin(adminId: String): ApiResult<List<ObjDocument>>? {

        return try {

            val httpResponse = clientWithoutDefaults.post(HTTPS_BASE_URL.plus(QUERYPATH)) {
                contentType(ContentType.Application.Json)
                setBody(
                    Query(
                        structuredQuery = StructuredQuery(
                            from = listOf(FromItem(collectionId = PROJECT)),
                            where = Where(
                                fieldFilter = FieldFilter(
                                    field = Field(
                                        fieldPath = "adminId"
                                    ),
                                    op = "EQUAL",
                                    value = Value(
                                        stringValue = adminId
                                    )
                                )
                            )
                        )
                    )
                )
            }
            logHttpResponse(httpResponse)

            if (httpResponse.status == HttpStatusCode.OK) {
                val listOfUsers = httpResponse.body<List<ObjListOfProjectItem?>?>()
                ApiResult(
                    success = true,
                    data = listOfUsers?.map { it?.document }?.filterNotNull(),
                    message = httpResponse.body<String>()
                )
            } else {
                ApiResult(
                    success = false,
                    data = emptyList(),
                    message = httpResponse.body<String>()
                )
            }

        } catch (e: Exception) {
            println("*** " + e.message)
            ApiResult(
                success = false,
                data = emptyList(),
                message = e.message
            )
        }
    }

    suspend fun addProject(projectName: String): ApiResult<ObjDocument> {
        return try {

            val httpResponse = client.post(PROJECT) {
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("documentId", projectName)
                }
                setBody(
                    AddAdminPayload(
                        fields = Fields(
                            projectId = StringValue(stringValue = projectName),
                            projectName = StringValue(stringValue = projectName),
                            adminId = StringValue(stringValue = getCurrentAdminId()),
                        )
                    )
                )
            }
            logHttpResponse(httpResponse)

            if (httpResponse.status == HttpStatusCode.OK) {
                val user = httpResponse.body<ObjDocument?>()
                ApiResult(
                    success = true,
                    data = user,
                    message = httpResponse.body<String>()
                )
            } else {
                ApiResult(
                    success = false,
                    data = null,
                    message = httpResponse.body<String>()
                )
            }

        } catch (e: Exception) {
            println("*** " + e.message)
            ApiResult(
                success = false,
                data = null,
                message = e.message
            )
        }

    }

    suspend fun addAdmin(adminId: String, adminPass: String, org: String?): ApiResult<ObjDocument> {
        return try {

            val httpResponse = client.post(ADMIN) {
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("documentId", adminId)
                }
                setBody(
                    AddAdminPayload(
                        fields = Fields(
                            adminId = StringValue(stringValue = adminId),
                            adminpass = StringValue(stringValue = adminPass.encrypt()),
                            org = StringValue(stringValue = org),
                            addedBy = StringValue(getCurrentAdminId())
                        )
                    )
                )
            }
            logHttpResponse(httpResponse)

            if (httpResponse.status == HttpStatusCode.OK) {
                val user = httpResponse.body<ObjDocument?>()
                ApiResult(
                    success = true,
                    data = user,
                    message = httpResponse.body<String>()
                )
            } else {
                ApiResult(
                    success = false,
                    data = null,
                    message = httpResponse.body<String>()
                )
            }

        } catch (e: Exception) {
            println("*** " + e.message)
            ApiResult(
                success = false,
                data = null,
                message = e.message
            )
        }

    }

    suspend fun addRelease(releases: ObjDocument): ApiResult<ObjDocument> {
        return try {

            val httpResponse = client.post(
                        PROJECT
                        .plus("/${releases.fields?.projectId?.stringValue}/")
                        .plus(RELEASES)) {
                contentType(ContentType.Application.Json)
                url {
                    parameters.append("documentId", releases.fields?.releaseId?.stringValue?:"")
                }
                setBody(
                    AddAdminPayload(
                        fields = releases.fields
                    )
                )
            }
            logHttpResponse(httpResponse)

            if (httpResponse.status == HttpStatusCode.OK) {
                val user = httpResponse.body<ObjDocument?>()
                ApiResult(
                    success = true,
                    data = user,
                    message = httpResponse.body<String>()
                )
            } else {
                ApiResult(
                    success = false,
                    data = null,
                    message = httpResponse.body<String>()
                )
            }

        } catch (e: Exception) {
            println("*** " + e.message)
            ApiResult(
                success = false,
                data = null,
                message = e.message
            )
        }
    }

    private suspend fun logHttpResponse(httpResponse: HttpResponse) {
        println("***********ResponseStart***********")
        println("URL : " + httpResponse.request.url)
        println("Method : " + httpResponse.request.method)
        println("Content : " + httpResponse.request.content)
        println("Status : " + httpResponse.status)
        println("ResponseTime : " + httpResponse.responseTime)
        println("Body : \n" + httpResponse.body<String>())
        println("***********ResponseEnd*************")
    }

    suspend fun deleteUser(user: ObjUser): ApiResult<ObjUser> {

        return try {

            val httpResponse = client.delete(USER.plus("/").plus(user.fields?.userId?.stringValue))
            logHttpResponse(httpResponse)

            if (httpResponse.status == HttpStatusCode.OK) {
                val objUser = httpResponse.body<ObjUser>()
                ApiResult(
                    success = true,
                    data = objUser,
                    message = httpResponse.body<String>()
                )
            } else {
                ApiResult(
                    success = false,
                    data = null,
                    message = httpResponse.body<String>()
                )
            }

        } catch (e: Exception) {
            println("*** " + e.message)
            ApiResult(
                success = false,
                data = null,
                message = e.message
            )
        }
    }

    suspend fun deleteProject(user: ObjDocument): ApiResult<ObjDocument> {

        return try {

            val httpResponse =
                client.delete(PROJECT.plus("/").plus(user.fields?.projectId?.stringValue))
            logHttpResponse(httpResponse)

            if (httpResponse.status == HttpStatusCode.OK) {
                val objUser = httpResponse.body<ObjDocument>()
                ApiResult(
                    success = true,
                    data = objUser,
                    message = httpResponse.body<String>()
                )
            } else {
                ApiResult(
                    success = false,
                    data = null,
                    message = httpResponse.body<String>()
                )
            }

        } catch (e: Exception) {
            println("*** " + e.message)
            ApiResult(
                success = false,
                data = null,
                message = e.message
            )
        }
    }

    suspend fun deleteRelease(user: ObjDocument): ApiResult<ObjDocument> {

        return try {

            val httpResponse = client.delete(RELEASES.plus(user.fields?.projectId))
            logHttpResponse(httpResponse)

            if (httpResponse.status == HttpStatusCode.OK) {
                val objUser = httpResponse.body<ObjDocument>()
                ApiResult(
                    success = true,
                    data = objUser,
                    message = httpResponse.body<String>()
                )
            } else {
                ApiResult(
                    success = false,
                    data = null,
                    message = httpResponse.body<String>()
                )
            }

        } catch (e: Exception) {
            println("*** " + e.message)
            ApiResult(
                success = false,
                data = null,
                message = e.message
            )
        }
    }

    suspend fun getAllReleasesForProject(objDocument: ObjDocument): ApiResult<List<ObjDocument>> {

        return try {

            val httpResponse = client.get {
                url {
                    appendPathSegments(
                        PROJECT,
                        objDocument.fields?.projectId?.stringValue ?: "",
                        RELEASES
                    )
                }
            }
            logHttpResponse(httpResponse)

            if (httpResponse.status == HttpStatusCode.OK) {
                val listOfUsers = httpResponse.body<ObjListOfReleases>()
                ApiResult(
                    success = true,
                    data = listOfUsers.documents?.filterNotNull(),
                    message = httpResponse.body<String>()
                )
            } else {
                ApiResult(
                    success = false,
                    data = emptyList(),
                    message = httpResponse.body<String>()
                )
            }

        } catch (e: Exception) {
            println("*** " + e.message)
            ApiResult(
                success = false,
                data = emptyList(),
                message = e.message
            )
        }
    }

    suspend fun getAllReleasesForAdmin(): ApiResult<List<ObjDocument>> {

        val allProjects = getAllProjectForAdmin(getCurrentAdminId() ?: "")?.data

        val apiResults = allProjects?.map { getAllReleasesForProject(it) }

        return ApiResult(
            success = true,
            data = apiResults?.flatMap { it.data ?: emptyList() }?.filter {
                (it.fields?.adminId?.stringValue?.equals(
                    getCurrentAdminId()
                ) == true) or (it.fields?.adminid?.stringValue?.equals(
                    getCurrentAdminId()
                ) == true)
            },
            message = ""
        )
    }

    suspend fun getAllReleasesForUser(userId: String): ApiResult<List<ObjDocument>> {

        val allProjects = getAllProjectForAdmin(getCurrentAdminId()?:"")?.data

        val apiResults = allProjects?.map { getAllReleasesForProject(it) }

        return ApiResult(
            success = true,
            data = apiResults?.flatMap { it.data ?: emptyList() }?.filter {
                (it.fields?.userId?.stringValue?.equals(
                    userId
                ) == true) or (it.fields?.userid?.stringValue?.equals(
                    userId
                ) == true)
            },
            message = ""
        )
    }


}


