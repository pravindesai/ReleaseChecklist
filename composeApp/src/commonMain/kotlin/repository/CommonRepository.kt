package repository

import kotlinx.coroutines.delay
import strings.UserType
import kotlin.time.Duration.Companion.seconds

object CommonRepository {
    suspend fun trySignIn(userId:String, userPassword:String, userType: UserType):Boolean{
        val tempId = "admin"
        val tempPassword = "admin"
        delay(3.seconds)

        return (tempId == userId) and (userPassword == tempPassword)
    }


}