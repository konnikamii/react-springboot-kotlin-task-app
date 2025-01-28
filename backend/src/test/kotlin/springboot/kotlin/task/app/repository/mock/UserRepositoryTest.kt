package springboot.kotlin.task.app.repository.mock

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.test.context.ActiveProfiles
import springboot.kotlin.task.app.model.User
import springboot.kotlin.task.app.repository.UserRepository


@ActiveProfiles("test")
internal class UserRepositoryTest {

    private lateinit var userRepository: UserRepository

    val users = listOf(
        User(1L, "user1", "user1@example.com", "password1"),
        User(2L, "user2", "user2@example.com", "password2"),
        User(3L, "user3", "user3@example.com", "password3")
    )

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userRepository = mock(UserRepository::class.java)
    }

    @Test
    fun `should return collection of users`() {
        `when`(userRepository.findAll()).thenReturn(users)

        val users = userRepository.findAll()

        assertThat(users.size).isGreaterThanOrEqualTo(3)
    }

    @Test
    fun `should return some mock data`() {
        `when`(userRepository.findAll()).thenReturn(users)

        val users = userRepository.findAll()

        assertThat(users).allMatch { (it.id ?: 0L) >= 1L }
        assertThat(users).anyMatch { it.username == "user1" }
        assertThat(users).anyMatch { it.email == "user1@example.com" }
    }

    @Test
    fun `should return user by username`() {
        val user = users[0]
        `when`(userRepository.findByUsername(user.username)).thenReturn(user)

        val result = userRepository.findByUsername(user.username)

        assertThat(result).isNotNull
        assertThat(result).isEqualTo(user)
    }
}
