package springboot.kotlin.task.app.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import springboot.kotlin.task.app.dto.TaskIn
import springboot.kotlin.task.app.dto.TasksIn
import springboot.kotlin.task.app.model.Task
import springboot.kotlin.task.app.model.User
import springboot.kotlin.task.app.repository.TaskRepository
import springboot.kotlin.task.app.utils.SortBy
import java.time.LocalDateTime

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val jwtService: JwtService
) {
    fun createTask(taskIn: TaskIn, token: String): Task? {
        val task = taskIn.toModel(jwtService.extractUserFromJwt(token))
        return taskRepository.save(task)
    }

    fun updateTask(id: Long, taskIn: TaskIn, token: String): Task? {
        val user = jwtService.extractUserFromJwt(token)
        val task = taskRepository.findById(id).orElse(null) ?: return null
        checkTaskOwner(task, user)
        println(taskIn)
        println(taskIn)
        println(taskIn)
        println(taskIn)
        val updatedTask = task.copy(
            title = taskIn.title,
            description = taskIn.description,
            completed = taskIn.completed,
            dueDate = taskIn.dueDate,
            updatedAt = LocalDateTime.now()
        )
        println(updatedTask)
        println(updatedTask)
        println(updatedTask)
        println(updatedTask)
        return taskRepository.save(updatedTask)
    }

    fun deleteById(id: Long, token: String) {
        val user = jwtService.extractUserFromJwt(token)
        val task = taskRepository.findById(id).orElse(null) ?: throw IllegalArgumentException("Task Not Found")
        checkTaskOwner(task, user)
        taskRepository.deleteById(id)
    }

    fun findById(id: Long, token: String): Task? {
        val user = jwtService.extractUserFromJwt(token)
        val task = taskRepository.findById(id).orElse(null)
        checkTaskOwner(task, user)
        return task
    }

    fun findPaginated(tasksIn: TasksIn, token: String): Page<Task> {
        val user = jwtService.extractUserFromJwt(token)
        val page = tasksIn.page
        val pageSize = tasksIn.page_size
        val sortBy = SortBy.valueOf(tasksIn.sort_by.toString())
        val sortType = tasksIn.sort_type.toString()
        println("Sort by: $sortBy")
        println("Sort type: $sortType")
        println("Page: $page")
        println("Page size: $pageSize")
        println("Sort by: ${sortBy.toString()}")


        val sort = if (sortType.equals("asc", ignoreCase = true)) {
            Sort.by(sortBy.toString()).ascending()
        } else {
            Sort.by(sortBy.toString()).descending()
        }
        val pageable = PageRequest.of(page - 1, pageSize, sort)
        return taskRepository.findByOwnerId(user.id!!, pageable)
    }

    private fun checkTaskOwner(task: Task, user: User) {
        if (task.owner.id != user.id) {
            throw IllegalArgumentException("Task Not Found")
        }
    }
}