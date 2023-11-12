package com.lee.remember.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendAddRequest(
    val description: String,
    val image: String,
    val mbti: String,
    val name: String,
    val phoneNumber: String
)

@Serializable
data class FriendAddResponse(
    @SerialName("result")
    val result: Result,
    @SerialName("resultCode")
    val resultCode: String
) {
    @Serializable
    data class Result(
        @SerialName("description")
        val description: String,
        @SerialName("eventList")
        val eventList: List<String>?,
        @SerialName("id")
        val id: Int,
        @SerialName("image")
        val image: Image,
        @SerialName("mbti")
        val mbti: String,
        @SerialName("name")
        val name: String,
        @SerialName("phoneNumber")
        val phoneNumber: String,
        @SerialName("userID")
        val userID: Int
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