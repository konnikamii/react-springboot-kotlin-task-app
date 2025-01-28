package springboot.kotlin.task.app.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import springboot.kotlin.task.app.dto.UserOut
import springboot.kotlin.task.app.dto.UserTaskOut
import springboot.kotlin.task.app.service.UserService

@RestController
@RequestMapping("/api")
class UserController(private val userService: UserService) {

    @GetMapping("/user/")
    fun getUser(@RequestHeader("Authorization") token: String): UserOut {
        return userService.findByToken(token)?.toDTO()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    }

    @PostMapping("/users/")
    fun getAllUsers(@RequestBody request: Map<String, String>): List<UserTaskOut> {
        val reqType = request["type"]
        return userService.findAllWithTasks(reqType)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Users not found")
    }

    @PutMapping("/change-password/")
    fun changePassword(
        @RequestParam("old_password") oldPassword: String,
        @RequestParam("new_password") newPassword: String,
        @RequestHeader("Authorization") token: String
    ): String {
        userService.updatePassword(oldPassword, newPassword, token)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password")
        return "Successfully updated password"
    }

    @DeleteMapping("/user/")
    fun deleteUser(@RequestHeader("Authorization") token: String): String {
        userService.delete(token)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        return "Successfully deleted user"
    }

}