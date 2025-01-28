package springboot.kotlin.task.app.dto

data class TasksOut(
    val total_tasks: Int,
    val tasks: List<TaskOut>
)