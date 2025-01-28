package springboot.kotlin.task.app.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import springboot.kotlin.task.app.model.Contact
import springboot.kotlin.task.app.repository.ContactRepository

@Service
class ContactService(
    private val contactRepository: ContactRepository,
    private val mailSender: JavaMailSender
) {
    fun createContact(contact: Contact): Contact? = contactRepository.save(contact)

    var yourEmail: String = "your-email@example.com"
    fun sendEmail(contact: Contact) {
        // Format the message
        val formattedMessage = String.format(
            "Name: %s\nEmail: %s\n\nMessage:\n%s",
            contact.name, contact.email, contact.message
        )
        val newMessage = SimpleMailMessage()

        newMessage.from = contact.email
        newMessage.setTo(yourEmail)
        newMessage.subject = contact.subject
        newMessage.text = formattedMessage
        mailSender.send(newMessage)
    }
}