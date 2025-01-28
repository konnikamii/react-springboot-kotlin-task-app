package springboot.kotlin.task.app.congif

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import springboot.kotlin.task.app.model.Task
import springboot.kotlin.task.app.model.User
import springboot.kotlin.task.app.repository.TaskRepository
import springboot.kotlin.task.app.repository.UserRepository
import springboot.kotlin.task.app.utils.Role
import java.time.LocalDate
import java.time.LocalDateTime

@Configuration
class DatabaseConfig(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository
) {
    @Bean
    fun dbCommandLineRunner(): CommandLineRunner {
        return CommandLineRunner {
            val date = LocalDateTime.now()
            val password = passwordEncoder.encode("qwerty123")
            val user1 = User(username = "username1", email = "asd@asd.asd", password = password, role = Role.USER)
            val user2 = User(username = "username2", email = "asd2@asd.asd", password = password, role = Role.USER)
            userRepository.saveAll(listOf(user1, user2))
            val tasks = listOf(
                Task(
                    title = "Task 1",
                    description = "Description 1",
                    completed = false,
                    dueDate = LocalDate.now().plusDays(7),
                    owner = user1
                ),
                Task(
                    title = "Task 2",
                    description = "Description 2",
                    completed = true,
                    dueDate = LocalDate.now(),
                    owner = user2
                ),
                Task(title = "Task 3", description = "Description 3", completed = true, dueDate = null, owner = user1),
                Task(
                    title = "Task 4",
                    description = "Description 4",
                    completed = true,
                    dueDate = LocalDate.now().plusDays(27),
                    owner = user2
                ),
                Task(
                    title = "Task 5",
                    description = "Description 5",
                    completed = false,
                    dueDate = LocalDate.now().plusDays(17),
                    owner = user1
                ),
                Task(
                    title = "Task 6",
                    description = "Description 6",
                    completed = false,
                    dueDate = LocalDate.now().plusDays(37),
                    owner = user2
                ),
                Task(
                    title = "Task 7",
                    description = "Description 7",
                    completed = false,
                    dueDate = LocalDate.now().plusDays(47),
                    owner = user1
                )
            )

            taskRepository.saveAll(tasks)

        }
    }
}