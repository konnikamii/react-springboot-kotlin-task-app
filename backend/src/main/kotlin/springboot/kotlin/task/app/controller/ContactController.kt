package springboot.kotlin.task.app.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import springboot.kotlin.task.app.dto.ContactIn
import springboot.kotlin.task.app.service.ContactService

@RestController
@RequestMapping("/api")
class ContactController(private val contactService: ContactService) {


    @PostMapping("/contact/")
    fun login(@ModelAttribute contactIn: ContactIn): String {
        val newContact = contactIn.toModel()
        contactService.createContact(newContact)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Contact not created")

        // Send email to MailHog
        try {
            contactService.sendEmail(newContact)
        } catch (ex: Exception) {
            println(ex.message)
            return "Contact form saved"
        }
        return "Contact form sent!"

    }


}