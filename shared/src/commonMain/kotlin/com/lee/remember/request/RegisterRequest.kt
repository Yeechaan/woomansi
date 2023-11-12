package com.lee.remember.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val name: String,
    val password: String,
    val image: String,
)

@Serializable
data class RegisterResponse(
    val result: Result?,
    val resultCode: String
) {
    @Serializable
    data class Result(
        val id: Int,
        val email: String,
        val friendList: List<String>,
        val image: Image,
        val name: String,
        val provider: String,
        val providerId: String?
    ) {
        @Serializable
        data class Image(
            val id: Int,
            val content: String
        )
    }
}