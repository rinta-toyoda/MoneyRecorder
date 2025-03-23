package com.moneyrecorder.backend.service

import com.moneyrecorder.backend.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User as AuthUser

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        // Make sure we have a non-null user input
        val email = username ?: throw UsernameNotFoundException("Email cannot be null")

        // Retrieve your user by email. This method is custom in your repository/DAO.
        val user = userRepository.findByEmail(email) ?: throw UsernameNotFoundException("User with email [$email] not found")

        // Convert your entity to a Spring Security UserDetails object
        return AuthUser(
            user.email,
            user.password,
            // Convert your roles/authorities to Spring Security's GrantedAuthority
            listOf(SimpleGrantedAuthority("ROLE_USER")),
        )
    }
}
