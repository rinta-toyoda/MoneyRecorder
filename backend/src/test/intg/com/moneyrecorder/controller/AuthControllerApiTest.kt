package com.moneyrecorder.controller

import com.moneyrecorder.dto.UserDTO
import com.moneyrecorder.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import com.moneyrecorder.util.PostgreSQLContainerInitializer
import org.junit.jupiter.api.BeforeEach

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class AuthControllerApiTest: PostgreSQLContainerInitializer() {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun setup() {
        userRepository.deleteAll()
    }

    @Test
    fun test_guest_can_register_user() {
        val userDTO = UserDTO(null, "example@example.com", "example")
        val savedUserDTO = webTestClient
            .post()
            .uri("/v1/auth/signup")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(UserDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(userDTO.email, savedUserDTO!!.email)
    }

    @Test
    fun test_guest_cannot_register_already_exiting_user() {
        val userDTO = UserDTO(null, "example@example.com", "example")
        val savedUserDTO = webTestClient
            .post()
            .uri("/v1/auth/signup")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(UserDTO::class.java)
            .returnResult()
            .responseBody

        webTestClient
            .post()
            .uri("/v1/auth/signup")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().isFound

        Assertions.assertEquals(userDTO.email, savedUserDTO!!.email)
    }
}