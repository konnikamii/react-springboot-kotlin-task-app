package springboot.kotlin.task.app.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import springboot.kotlin.task.app.dto.TaskIn
import springboot.kotlin.task.app.dto.TaskOut
import springboot.kotlin.task.app.dto.TasksIn
import springboot.kotlin.task.app.dto.TasksOut
import springboot.kotlin.task.app.service.TaskService

@RestController
@RequestMapping("/api")
class TaskController(private val taskService: TaskService) {

    @PostMapping("/tasks/")
    fun getTasks(@RequestHeader("Authorization") token: String, @RequestBody request: TasksIn): TasksOut {
        val result = taskService.findPaginated(request, token)
        return TasksOut(result.totalElements.toInt(), result.content.map { it.toDTO() })
    }

    @GetMapping("/task/{id}")
    fun getTask(@RequestHeader("Authorization") token: String, @PathVariable id: Long): TaskOut =
        taskService.findById(id, token)?.toDTO()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")

    @PostMapping("/task/")
    fun createTask(@RequestHeader("Authorization") token: String, @RequestBody taskIn: TaskIn): String {
        taskService.createTask(taskIn, token)?.toDTO()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create task")
        return "Successfully created task"
    }

    @PutMapping("/task/{id}")
    fun updateTask(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: Long,
        @RequestBody taskIn: TaskIn
    ): String {
        taskService.updateTask(id, taskIn, token)?.toDTO()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")
        return "Successfully updated task"
    }

    @DeleteMapping("/task/{id}")
    fun deleteTask(@RequestHeader("Authorization") token: String, @PathVariable id: Long): String {
        taskService.findById(id, token) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")
        taskService.deleteById(id, token)
        return "Successfully deleted task"
    }


}