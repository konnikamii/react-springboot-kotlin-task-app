package springboot.kotlin.task.app.dto

import springboot.kotlin.task.app.model.Task
import springboot.kotlin.task.app.model.User
import java.time.LocalDate

data class TaskIn(
    val title: String,
    val description: String,
    val completed: Boolean,
    val dueDate: LocalDate?,
) {
    fun toModel(owner: User): Task = Task(
        title = title,
        description = description,
        completed = completed,
        dueDate = dueDate,
        owner = owner
    )
}