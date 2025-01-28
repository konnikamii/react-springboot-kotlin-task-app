package springboot.kotlin.task.app.model

import jakarta.persistence.*
import springboot.kotlin.task.app.dto.TaskOut
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null,
    @Column(nullable = false)
    var title: String,
    @Column(length = 1500)
    var description: String,
    @Column(nullable = false)
    var completed: Boolean,
    var dueDate: LocalDate? = null,

    var createdAt: LocalDateTime? = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    var owner: User

) {
    fun toDTO(): TaskOut = TaskOut(
        id = this.id ?: 0,
        title = this.title,
        description = this.description,
        completed = this.completed,
        dueDate = this.dueDate,
        updatedAt = this.updatedAt!!,
        createdAt = this.createdAt!!
    )
}
