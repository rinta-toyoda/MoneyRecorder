package com.moneyrecorder.backend.exception

class UserAlreadyExistsException(
    message: String,
) : RuntimeException(message)
