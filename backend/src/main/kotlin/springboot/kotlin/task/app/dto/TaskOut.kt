package springboot.kotlin.task.app.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class TaskOut(
    val id: Long,
    val title: String,
    val description: String,
    val completed: Boolean,
    val dueDate: LocalDate?,
    val updatedAt: LocalDateTime,
    val createdAt: LocalDateTime

)