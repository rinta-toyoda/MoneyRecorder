package com.moneyrecorder.backend.dto

import jakarta.validation.constraints.NotBlank

data class UserDTO(
    val id: Int?,
    @get:NotBlank(message = "userDTO.email must not be blank")
    val email: String,
    @get:NotBlank(message = "userDTO.password must not be blank")
    val password: String,
)
