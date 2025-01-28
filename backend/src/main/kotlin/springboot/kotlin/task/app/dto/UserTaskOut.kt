package springboot.kotlin.task.app.dto

import springboot.kotlin.task.app.utils.Role
import java.time.LocalDateTime

data class UserTaskOut(
    val id: Long,
    val username: String,
    val email: String,
    val role: Role,
    val tasks: List<TaskOut>,
    val updatedAt: LocalDateTime,
    val createdAt: LocalDateTime
)