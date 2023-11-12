package com.lee.remember.request

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val email: String,
    val password: String,
)

@Serializable
data class SignInResponse(
    val result: Result?,
    val resultCode: String
) {
    @Serializable
    data class Result(
        val userId: Int,
        val jwtToken: String
    )
}