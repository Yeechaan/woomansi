package com.lee.remember.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class LoginResponse(
    val result: Result?,
    val resultCode: String
) {
    @Serializable
    data class Result(
        val jwtToken: String
    )
}