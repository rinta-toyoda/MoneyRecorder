package com.moneyrecorder.repository

import com.moneyrecorder.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int> {
    fun findByEmail(email: String): User?
}
