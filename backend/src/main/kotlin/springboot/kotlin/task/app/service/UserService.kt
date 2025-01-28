package springboot.kotlin.task.app.service

import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import springboot.kotlin.task.app.dto.UserTaskOut
import springboot.kotlin.task.app.model.User
import springboot.kotlin.task.app.repository.TaskRepository
import springboot.kotlin.task.app.repository.UserRepository
import java.time.LocalDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder,
    private val taskRepository: TaskRepository,
    private val jwtService: JwtService
) {
    fun createUser(user: User): User? {
        val foundByUsername = userRepository.findByUsername(user.username)
        val foundByEmail = userRepository.findByEmail(user.email)
        if (foundByUsername != null) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists")
        if (foundByEmail != null) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists")
        val updated = user.copy(password = encoder.encode(user.password))
        return userRepository.save(updated)
    }

    fun findAll(): Collection<User> = userRepository.findAll()

    fun updatePassword(oldPassword: String, newPassword: String, token: String): User? {
        val user = jwtService.extractUserFromJwt(token)
        return if (encoder.matches(oldPassword, user.password)) {
            val updated = user.copy(
                password = encoder.encode(newPassword),
                updatedAt = LocalDateTime.now()
            )
            userRepository.save(updated)
            updated
        } else null
    }

    fun findByToken(token: String): User? {
        return jwtService.extractUserFromJwt(token)
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun findAllWithTasks(reqType: String?): List<UserTaskOut> {
        return userRepository.findAll().mapNotNull { user ->
            user.id?.let { id ->
                UserTaskOut(
                    id = id,
                    username = user.username,
                    email = user.email,
                    role = user.role,
                    updatedAt = user.updatedAt!!,
                    createdAt = user.createdAt!!,
                    tasks = if (reqType.equals("user_tasks")) {
                        taskRepository.findByOwnerId(id).map { task ->
                            task.toDTO()
                        }
                    } else {
                        emptyList()
                    }
                )
            }
        }
    }

    fun delete(token: String): Boolean {
        val user = jwtService.extractUserFromJwt(token)
        return run {
            userRepository.delete(user)
            true
        }
    }
}