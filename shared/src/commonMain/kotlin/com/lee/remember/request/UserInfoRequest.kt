package com.lee.remember.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoRequest(
    val email: String,
    val password: String,
    val name: String,
    val phoneNumber: String,
    val profileImage: String,
)

@Serializable
data class UserInfoResponse(
    @SerialName("result")
    val result: Result?,
    @SerialName("resultCode")
    val resultCode: String
) {
    @Serializable
    data class Result(
        @SerialName("id")
        val id: Int,
        @SerialName("email")
        val email: String,
        @SerialName("name")
        val name: String?,
        @SerialName("phoneNumber")
        val phoneNumber: String?,
        @SerialName("profileImage")
        val profileImage: ProfileImage?,
        @SerialName("authProvider")
        val authProvider: String,
        @SerialName("providerId")
        val providerId: String?
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