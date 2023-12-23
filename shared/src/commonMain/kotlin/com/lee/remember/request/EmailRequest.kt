package com.lee.remember.request

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val email: String
)

@Serializable
data class EmailResponse(
    val result: Result?,
    val resultCode: String
) {
    @Serializable
    data class Result(
        val code: String
    )
}

@Serializable
data class EmailCheckRequest(
    val email: String,
    val code: String
)

@Serializable
data class EmailCheckResponse(
    val result: Result?,
    val resultCode: String
) {
    @Serializable
    data class Result(
        val email: String,
        val result: Boolean
    )
}