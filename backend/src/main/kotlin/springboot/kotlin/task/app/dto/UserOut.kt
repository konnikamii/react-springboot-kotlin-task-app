package springboot.kotlin.task.app.dto

import springboot.kotlin.task.app.utils.Role
import java.time.LocalDateTime

data class UserOut(
    val id: Long,
    val username: String,
    val email: String,
    val role: Role,
    val updatedAt: LocalDateTime,
    val createdAt: LocalDateTime
)
