package springboot.kotlin.task.app.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import springboot.kotlin.task.app.dto.AuthIn
import springboot.kotlin.task.app.dto.AuthOut
import springboot.kotlin.task.app.dto.UserIn
import springboot.kotlin.task.app.dto.UserOut
import springboot.kotlin.task.app.service.AuthenticationService
import springboot.kotlin.task.app.service.UserService

@RestController
@RequestMapping("/api")
class AuthController(private val userService: UserService, private val authenticationService: AuthenticationService) {
    @PostMapping("/register/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@ModelAttribute userIn: UserIn): UserOut =
        userService.createUser(userIn.toModel())?.toDTO()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create user")

    @PostMapping("/login/")
    fun login(@ModelAttribute authIn: AuthIn): AuthOut =
        authenticationService.authenticate(authIn)

    @PostMapping("/auth/")
    fun auth(): String =
        "Authenticated"
}