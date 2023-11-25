package com.lee.remember.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoRequest(
    val name: String,
    val image: String,
)

@Serializable
data class UserInfoResponse(
    @SerialName("result")
    val result: Result,
    @SerialName("resultCode")
    val resultCode: String
) {
    @Serializable
    data class Result(
        @SerialName("email")
        val email: String,
        @SerialName("friendList")
        val friendList: List<String>,
        @SerialName("id")
        val id: Int,
        @SerialName("image")
        val image: Image,
        @SerialName("name")
        val name: String,
        @SerialName("provider")
        val provider: String,
        @SerialName("providerId")
        val providerId: String?
    ) {
        @Serializable
        data class Image(
            @SerialName("content")
            val content: String,
            @SerialName("id")
            val id: Int
        )
    }
}