package springboot.kotlin.task.app.dto

data class AuthOut(
    val access_token: String,
    val token_type: String = "Bearer",
)