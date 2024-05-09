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
import kotlinx.serialization.json.Json
import repository.models.ApiResult
import repository.models.Field
import repository.models.FieldFilter
import repository.models.FromItem
import repository.models.Query
import repository.models.StructuredQuery
import repository.models.Value
import repository.models.Where
import repository.models.data.IntUser
import repository.models.data.ObjAdmin
import repository.models.data.ObjListOfUsers
import repository.models.data.ObjListOfUsersItem
import repository.models.data.ObjUser
import strings.UserType
import kotlin.time.Duration.Companion.seconds

object CommonRepository {
    private var currentUser:IntUser?  = null

    const val BASE_URL = "firestore.googleapis.com/v1/projects/releasechecklist-kmm-app/databases/(default)/documents"
    const val HTTPS_BASE_URL = "https://$BASE_URL"
    const val ADMIN = "admin"
    const val USER = "user"

    const val QUERYPATH = ":runQuery"

    private val client = HttpClient(){
        install(Resources)
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging){
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        defaultRequest {
            host = BASE_URL
            url{
                protocol = URLProtocol.HTTPS
            }
        }
    }

    private val clientWithoutDefaults = HttpClient(){
        install(Resources)
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }


    fun getLoggedInUser() = currentUser

    fun setCurrentUser(user: IntUser?) {
        currentUser = user
    }

    suspend fun trySignIn(userId:String, userPassword:String, userType: UserType):Pair<Boolean, IntUser?>{
        when(userType){
            UserType.ADMIN -> {
                return try {
                    val httpResponse =  client.get {
                        url {
                            appendPathSegments(ADMIN, userId)
                        }
                    }
                    logHttpResponse(httpResponse)
                    if (httpResponse.status == HttpStatusCode.OK){
                        val adminOfMatchingId = httpResponse.body<ObjAdmin?>()
                        val isLoggedIn = adminOfMatchingId?.fields?.adminid?.stringValue.equals(userId) and adminOfMatchingId?.fields?.adminpass?.stringValue.equals(userPassword)
                        setCurrentUser(adminOfMatchingId.takeIf { isLoggedIn })
                        Pair(isLoggedIn, adminOfMatchingId)
                    }else{
                        setCurrentUser(null)
                        Pair(false, null)
                    }

                }catch (e:Exception){
                    setCurrentUser(null)
                    println("*** "+e.message)
                    Pair(false, null)
                }
            }
            UserType.USER -> {
                return try {
                    val httpResponse =  client.get {
                        url {
                            appendPathSegments(USER, userId)
                        }
                    }
                    logHttpResponse(httpResponse)
                    if (httpResponse.status == HttpStatusCode.OK){
                        val userOfMatchingId = httpResponse.body<ObjUser?>()
                        val isLoggedIn =userOfMatchingId?.fields?.userid?.stringValue.equals(userId) and userOfMatchingId?.fields?.userpass?.stringValue.equals(userPassword)
                        setCurrentUser(userOfMatchingId.takeIf { isLoggedIn })
                        Pair(isLoggedIn, userOfMatchingId)

                    }else{
                        setCurrentUser(null)
                        Pair(false, null)
                    }

                }catch (e:Exception){
                    setCurrentUser(null)
                    println("*** "+e.message)
                    Pair(false, null)
                }
            }
        }
    }

    suspend fun signOut():Boolean{
        delay(2.seconds)
        setCurrentUser(null)
        return true
    }

    suspend fun addUser(user: Any):Boolean{
        delay(2.seconds)
        return true
    }

    suspend fun removeUser(userId:String):Boolean{
        delay(2.seconds)
        return true
    }

    suspend fun getAllUsersForAdmin(adminId:String): ApiResult<List<ObjUser>>?{

        return try {

            val httpResponse =  clientWithoutDefaults.post(HTTPS_BASE_URL.plus(QUERYPATH)) {
                contentType(ContentType.Application.Json)
                setBody(Query(
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
                ))
            }
            logHttpResponse(httpResponse)

            if (httpResponse.status == HttpStatusCode.OK){
                val listOfUsers = httpResponse.body<List<ObjListOfUsersItem?>?>()
                ApiResult(
                    success = true,
                    data = listOfUsers?.map { it?.document }?.filterNotNull(),
                    message = httpResponse.body<String>()
                )
            }else{
                ApiResult(
                    success = false,
                    data = emptyList(),
                    message = httpResponse.body<String>()
                )
            }

        }catch (e:Exception){
            println("*** "+e.message)
            ApiResult(
                success = false,
                data = emptyList(),
                message = e.message
            )
        }
    }

    suspend fun addProject(project: Any):Boolean{
        delay(2.seconds)
        return true
    }

    suspend fun removeProject(projectId:String):Boolean{
        delay(2.seconds)
        return true
    }

    suspend fun addRelease(releases: Any):Boolean{
        delay(2.seconds)
        return true
    }

    suspend fun removeRelease(releaseId:String):Boolean{
        delay(2.seconds)
        return true
    }

    private suspend fun logHttpResponse(httpResponse: HttpResponse) {
        println("***********ResponseStart***********")
        println("URL : "+httpResponse.request.url)
        println("Method : "+httpResponse.request.method)
        println("Content : "+httpResponse.request.content)
        println("Status : "+httpResponse.status)
        println("ResponseTime : "+httpResponse.responseTime)
        println("Body : \n"+httpResponse.body<String>())
        println("***********ResponseEnd*************")
    }
}
