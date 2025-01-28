package springboot.kotlin.task.app.utils


enum class Role {
    USER, ADMIN
}

enum class SortBy {
    title,
    description,
    completed,
    dueDate,
    createdAt,
    updatedAt
}

enum class SortType {
    asc, desc
}