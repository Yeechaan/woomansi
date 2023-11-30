package com.lee.remember.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val email: String,
    val password: String,
    val name: String = "",
    val phoneNumber: String = "",
    val profileImage: String = "",
)

@Serializable
data class SignupResponse(
    @SerialName("result")
    val result: Result?,
    @SerialName("resultCode")
    val resultCode: String
) {
    @Serializable
    data class Result(
        @SerialName("email")
        val email: String,
        @SerialName("id")
        val id: Int,
        @SerialName("jwtToken")
        val jwtToken: String,
        @SerialName("name")
        val name: String?,
        @SerialName("phoneNumber")
        val phoneNumber: String?,
        @SerialName("profileImage")
        val profileImage: ProfileImage,
        @SerialName("provider")
        val provider: String,
        @SerialName("providerId")
        val providerId: Int?
    ) {
        @Serializable
        data class ProfileImage(
            @SerialName("id")
            val id: Int,
            @SerialName("image")
            val image: String?
        )
    }
}