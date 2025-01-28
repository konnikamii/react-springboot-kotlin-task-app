package springboot.kotlin.task.app.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.NoHandlerFoundException

@ControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException): ResponseEntity<Map<String, String?>> {
        val response: MutableMap<String, String?> = HashMap()
        response["detail"] = ex.reason
        return ResponseEntity(response, ex.statusCode)
    }

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(ex: NoHandlerFoundException): ResponseEntity<Map<String, String?>> {
        val response: MutableMap<String, String?> = HashMap()
        response["detail"] = "Resource not found"
        return ResponseEntity(response, ex.statusCode)
    }
}