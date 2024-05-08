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
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import repository.models.ObjAdmin
import repository.models.Project
import repository.models.Releases
import repository.models.User
import strings.UserType
import kotlin.time.Duration.Companion.seconds

object CommonRepository {
    const val BASE_URL = "firestore.googleapis.com/v1/projects/releasechecklist-kmm-app/databases/(default)/documents"
    const val ADMIN_LOGIN = "admin"

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


    private var currentUserType:UserType?  = null

    fun getLoggedInUserType() = currentUserType

    fun setCurrentUserType(userType: UserType?) {
        currentUserType = userType
    }

    suspend fun trySignIn(userId:String, userPassword:String, userType: UserType):Boolean{
        when(userType){
            UserType.ADMIN -> {
                return try {
                    val httpResponse =  client.get {
                        url {
                            appendPathSegments(ADMIN_LOGIN, userId)
                        }
                    }
                    logHttpResponse(httpResponse)
                    if (httpResponse.status == HttpStatusCode.OK){
                        val adminOfMatchingId = httpResponse.body<ObjAdmin>()
                        adminOfMatchingId.fields?.adminid?.stringValue.equals(userId) and adminOfMatchingId.fields?.adminpass?.stringValue.equals(userPassword)
                    }else{
                        false
                    }

                }catch (e:Exception){
                    println("*** "+e.message)
                    false
                }
            }
            UserType.USER -> {
                return false
            }
        }
    }

    suspend fun signOut():Boolean{
        delay(2.seconds)
        return true
    }

    suspend fun addUser(user: User):Boolean{
        delay(2.seconds)
        return true
    }

    suspend fun removeUser(userId:String):Boolean{
        delay(2.seconds)
        return true
    }

    suspend fun getAllUsers(user: User):List<User>{
        delay(2.seconds)
        return listOf(
            User("1", "ABCD", "USER", "ory34985", "5384958394"),
            User("2", "DCBA", "USER", "lksdlskl", "rewoewoiro")
        )
    }

    suspend fun addProject(project: Project):Boolean{
        delay(2.seconds)
        return true
    }

    suspend fun removeProject(projectId:String):Boolean{
        delay(2.seconds)
        return true
    }

    suspend fun addRelease(releases: Releases):Boolean{
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
        println("Status : "+httpResponse.status)
        println("ResponseTime : "+httpResponse.responseTime)
        println("Body : \n"+httpResponse.body<String>())
        println("***********ResponseEnd*************")
    }
}
