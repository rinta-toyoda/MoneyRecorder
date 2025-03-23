package com.moneyrecorder.config

import com.moneyrecorder.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
) {
    @Bean
    fun authManager(http: HttpSecurity): AuthenticationManager {
        val authBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder)
        return authBuilder.build()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests {
                // Allow anyone to register
                it.requestMatchers("/v1/auth/signup").permitAll()

                // Public endpoints
                it.requestMatchers("/public/**").permitAll()

                // All other endpoints require authentication
                it.anyRequest().authenticated()
            }.formLogin(withDefaults())
            .logout {
                it.permitAll()
            }
            // Optionally enable CSRF, etc.
            .csrf {
                it.ignoringRequestMatchers("/v1/auth/signup")
                it.disable()
            }

        return http.build()
    }
}
