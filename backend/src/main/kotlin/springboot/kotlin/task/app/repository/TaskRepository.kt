package springboot.kotlin.task.app.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import springboot.kotlin.task.app.model.Task

interface TaskRepository : JpaRepository<Task, Long> {

    fun findByOwnerId(ownerId: Long, pageable: Pageable): Page<Task>
    fun findByOwnerId(ownerId: Long): Collection<Task>

}