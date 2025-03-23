package com.moneyrecorder.backend.repository

import com.moneyrecorder.backend.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int> {
    fun findByEmail(email: String): User?
}
