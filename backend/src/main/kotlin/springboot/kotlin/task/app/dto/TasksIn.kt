package springboot.kotlin.task.app.dto

import springboot.kotlin.task.app.utils.SortBy
import springboot.kotlin.task.app.utils.SortType

data class TasksIn(
    val page: Int,
    val page_size: Int,
    val sort_by: SortBy,
    val sort_type: SortType
)
