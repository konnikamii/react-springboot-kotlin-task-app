package springboot.kotlin.task.app.model

import jakarta.persistence.*
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import springboot.kotlin.task.app.dto.UserOut
import springboot.kotlin.task.app.utils.Role
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null,
    var username: String,
    var email: String,
    var password: String,
//
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,

    var updatedAt: LocalDateTime? = LocalDateTime.now(),

    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER,

    ) {

    fun toDTO(): UserOut? = UserOut(
        id = this.id ?: 0,
        username = this.username,
        email = this.email,
        role = this.role,
        updatedAt = this.updatedAt!!,
        createdAt = this.createdAt!!
    )

    init {
        validateUsername(username)?.let {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, it)
        }
        validateEmail(email)?.let {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, it)
        }
        validatePassword(password)?.let {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, it)
        }
    }

    private fun validateUsername(value: String?): String? {
        var newErr: String? = null
        if (value != null) {
            val cleanedValue = value.replace("\\s+".toRegex(), "")
            when {
                cleanedValue.length < 5 -> newErr = "Username should be at least 5 characters!"
                cleanedValue.length > 30 -> newErr = "Username should be less than 30 characters!"
                !cleanedValue.matches("^[a-zA-Z0-9]+$".toRegex()) -> newErr =
                    "Username should only contain letters and numbers!"
            }
        } else {
            newErr = "Please provide a username!"
        }
        return newErr
    }

    private fun validateEmail(value: String?): String? {
        var newErr: String? = null
        if (value != null) {
            val cleanedValue = value.replace("\\s+".toRegex(), "")
            if (!cleanedValue.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex())) {
                newErr = "Invalid email format!"
            }
        } else {
            newErr = "Please provide an email!"
        }
        return newErr
    }

    private fun validatePassword(value: String?): String? {
        var newErr: String? = null
        if (value != null) {
            val cleanedValue = value.replace("\\s+".toRegex(), "")
            when {
                cleanedValue.length < 8 -> newErr = "Password should be at least 8 characters!"
                !cleanedValue.matches(".*[a-zA-Z].*".toRegex()) -> newErr =
                    "Password should contain at least one letter!"

                !cleanedValue.matches(".*[0-9].*".toRegex()) -> newErr = "Password should contain at least one digit!"
                // Uncomment the following lines if you want to enforce special character validation
                // !cleanedValue.matches(".*[!@#$%^&*].*".toRegex()) -> newErr = "Password should contain at least one special character!"
            }
        } else {
            newErr = "Please provide a password!"
        }
        return newErr
    }
}

