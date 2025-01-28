package springboot.kotlin.task.app.controller

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.JsonPath
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
internal class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    private lateinit var authToken: String
    @Serializable
    private data class Token(val access_token: String, val token_type: String)
    @BeforeEach
    fun setUp() {
        // Create user
        mockMvc.post("/api/register/") {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("username", "user1")
            param("email", "user1@example.com")
            param("password", "password123")
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                content { contentType("application/json") }
                jsonPath("$.username") { value("user1") }
            }
        // Login
        val result = mockMvc.post("/api/login/") {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("username", "user1")
            param("password", "password123")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType("application/json") }
                jsonPath("$.token_type") { value("Bearer") }
            }
            .andReturn()
        val response = result.response.contentAsString
        authToken = Json.decodeFromString<Token>(response).access_token
    }
    @AfterEach
    fun tearDown() {
        // Delete user
        mockMvc.delete("/api/user/") {
            header("Authorization", "Bearer $authToken")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }
    @Test
    fun `should create user`() {
        mockMvc.post("/api/register/") {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("username", "user2")
            param("email", "user2@example.com")
            param("password", "password123")
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                content { contentType("application/json") }
                jsonPath("$.username") { value("user2") }
            }

    }

    @Test
    fun `should return all users`() {
        mockMvc.post("/api/users/") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $authToken")
            content = """{"type": "user_tasks"}"""
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType("application/json") }
                jsonPath("[0].id") { value(1L) }
            }

    }

    @Test
    fun `should change password`() {
        mockMvc.put("/api/change-password/") {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("old_password", "password123")
            param("new_password", "newpassword123")
            header("Authorization", "Bearer $authToken")
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { string("Successfully updated password") }
            }
    }
}