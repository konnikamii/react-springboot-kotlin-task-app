package springboot.kotlin.task.app.repository

import org.springframework.data.jpa.repository.JpaRepository
import springboot.kotlin.task.app.model.User

interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?

}