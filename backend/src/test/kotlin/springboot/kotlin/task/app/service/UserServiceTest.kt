package springboot.kotlin.task.app.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.server.ResponseStatusException
import springboot.kotlin.task.app.model.User
import springboot.kotlin.task.app.repository.TaskRepository
import springboot.kotlin.task.app.repository.UserRepository

@ActiveProfiles("test")
internal class UserServiceTest {

    private val userRepository: UserRepository = mockk(relaxed = true)
    private val encoder: PasswordEncoder = mockk(relaxed = true)
    private val taskRepository: TaskRepository = mockk(relaxed = true)
    private val jwtService: JwtService = mockk(relaxed = true)
    private val userService = UserService(userRepository, encoder, taskRepository, jwtService)

    @Test
    fun `should throw exception if username exists`() {
        val user = User(username = "testuser", email = "test@example.com", password = "password1")
        every { userRepository.findByUsername(user.username) } returns user

        assertThatThrownBy { userService.createUser(user) }
            .isInstanceOf(ResponseStatusException::class.java)
            .hasMessage("400 BAD_REQUEST \"Username already exists\"")

    }

    @Test
    fun `should save user`() {
        val user = User(username = "testuser", email = "test@example.com", password = "password1")
        every { userRepository.findByUsername(user.username) } returns null
        every { userRepository.findByEmail(user.email) } returns null
        every { encoder.encode(user.password) } returns "encodedPassword1"
        every { userRepository.save(any()) } returns user.copy(password = "encodedPassword1")

        val result = userService.createUser(user)

        assertThat(result).isNotNull
        assertThat(result?.password).isEqualTo("encodedPassword1")
        verify(exactly = 1) { userRepository.save(any()) }
    }

    @Test
    fun `should return all users`() {
        val users = listOf(User(username = "testuser", email = "test@example.com", password = "password1"))
        every { userRepository.findAll() } returns users

        val result = userService.findAll()

        assertThat(result).isEqualTo(users)
        verify(exactly = 1) { userRepository.findAll() }
    }

    @Test
    fun `should update password`() {
        val user = User(username = "testuser", email = "test@example.com", password = "oldPassword1")
        val token = "token"
        every { jwtService.extractUserFromJwt(token) } returns user
        every { encoder.matches("oldPassword1", user.password) } returns true
        every { encoder.encode("newPassword1") } returns "encodedNewPassword1"
        every { userRepository.save(any()) } returns user.copy(password = "encodedNewPassword1")

        val result = userService.updatePassword("oldPassword1", "newPassword1", token)

        assertThat(result).isNotNull
        assertThat(result?.password).isEqualTo("encodedNewPassword1")
        verify(exactly = 1) { userRepository.save(any()) }
    }

    @Test
    fun `should return null if old password does not match`() {
        val user = User(username = "testuser", email = "test@example.com", password = "oldPassword1")
        val token = "token"
        every { jwtService.extractUserFromJwt(token) } returns user
        every { encoder.matches("oldPassword1", user.password) } returns false

        val result = userService.updatePassword("oldPassword1", "newPassword1", token)

        assertThat(result).isNull()
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `should return user from token`() {
        val user = User(username = "testuser", email = "test@example.com", password = "password1")
        val token = "token"
        every { jwtService.extractUserFromJwt(token) } returns user

        val result = userService.findByToken(token)

        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `should return user from username`() {
        val user = User(username = "testuser", email = "test@example.com", password = "password1")
        every { userRepository.findByUsername(user.username) } returns user

        val result = userService.findByUsername(user.username)

        assertThat(result).isEqualTo(user)
    }
}