package springboot.kotlin.task.app.dto

import springboot.kotlin.task.app.model.Contact

data class ContactIn(
    val name: String,
    val email: String,
    val subject: String,
    val message: String,
) {
    fun toModel(): Contact = Contact(
        name = name,
        email = email,
        subject = subject,
        message = message,
    )
}
