package com.moneyrecorder.controller

import com.moneyrecorder.dto.UserDTO
import com.moneyrecorder.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/auth")
@Validated
class AuthController(
    val userService: AuthService,
) {
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(
        @RequestBody @Valid userDTO: UserDTO,
    ): UserDTO = userService.addUser(userDTO)
}
