package com.moneyrecorder.service

import com.moneyrecorder.dto.UserDTO
import com.moneyrecorder.entity.User
import com.moneyrecorder.exception.UserAlreadyExistsException
import com.moneyrecorder.repository.UserRepository
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
        val user = User(null, userDTO.email, hashedPassword)
        // Save User
        userRepository.save(user)

        return user.let {
            UserDTO(it.id, it.email, it.password)
        }
    }
}
