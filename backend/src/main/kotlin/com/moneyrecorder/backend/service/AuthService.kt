package com.moneyrecorder.backend.service

import com.moneyrecorder.backend.dto.UserDTO
import com.moneyrecorder.backend.entity.User
import com.moneyrecorder.backend.exception.UserAlreadyExistsException
import com.moneyrecorder.backend.repository.UserRepository
import mu.KLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
) {
    private val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    companion object : KLogging()

    fun addUser(userDTO: UserDTO): UserDTO {
        userRepository.findByEmail(userDTO.email)?.let {
            throw UserAlreadyExistsException("User already exists")
        }

        // Generate Hashed Password
        val hashedPassword = passwordEncoder.encode(userDTO.password)

        // Create User
        val jwtUser = User(null, userDTO.email, hashedPassword)
        // Save User
        jwtUser.let { userRepository.save(it) }

        return jwtUser.let {
            UserDTO(it.id, it.email, it.password)
        }
    }
}
