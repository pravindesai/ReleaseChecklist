package repository

import kotlinx.coroutines.delay
import repository.models.Project
import repository.models.Releases
import repository.models.User
import strings.UserType
import kotlin.time.Duration.Companion.seconds

object CommonRepository {
    private var currentUserType:UserType?  = null

    fun getLoggedInUserType() = currentUserType

    fun setCurrentUserType(userType: UserType?) {
        currentUserType = userType
    }

    suspend fun trySignIn(userId:String, userPassword:String, userType: UserType):Boolean{
        val tempId = "admin"
        val tempPassword = "admin"
        delay(2.seconds)

        return (tempId == userId) and (userPassword == tempPassword)
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
}
