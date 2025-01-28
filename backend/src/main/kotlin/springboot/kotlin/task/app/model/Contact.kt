package springboot.kotlin.task.app.model

import jakarta.persistence.*
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Entity
@Table(name = "contacts")
data class Contact(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val email: String,
    @Column(nullable = false)
    val subject: String,
    @Column(nullable = false, length = 1500)
    val message: String,

    var createdAt: LocalDateTime? = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = LocalDateTime.now(),


    ) {
    init {
        validateEmail(email)?.let {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, it)
        }
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

}
