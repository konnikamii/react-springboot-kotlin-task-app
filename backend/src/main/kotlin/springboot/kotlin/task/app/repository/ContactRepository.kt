package springboot.kotlin.task.app.repository

import org.springframework.data.jpa.repository.JpaRepository
import springboot.kotlin.task.app.model.Contact

interface ContactRepository : JpaRepository<Contact, Long> {

}