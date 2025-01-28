package springboot.kotlin.task.app.dto

import springboot.kotlin.task.app.model.User
import springboot.kotlin.task.app.utils.Role

data class UserIn(
    val username: String,
    val email: String,
    val password: String,
) {

    fun toModel(): User = User(
        username = username,
        email = email,
        password = password,
        role = Role.USER
    )
}
