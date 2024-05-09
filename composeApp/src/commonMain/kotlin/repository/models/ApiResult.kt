package repository.models

data class ApiResult<T>(
    val success:Boolean? = false,
    val data:T? = null,
    val message:String?=null
)