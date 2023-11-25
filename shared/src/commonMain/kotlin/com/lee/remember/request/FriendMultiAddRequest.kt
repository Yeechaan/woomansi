package com.lee.remember.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendMultiAddRequest(
    val friends: List<Friend>
) {
    @Serializable
    data class Friend(
        @SerialName("name")
        val name: String,
        @SerialName("phoneNumber")
        val phoneNumber: String,
        @SerialName("image")
        val image: String
    )
}

@Serializable
data class FriendMultiAddResponse(
    @SerialName("result")
    val result: List<Result>,
    @SerialName("resultCode")
    val resultCode: String
) {
    @Serializable
    data class Result(
        @SerialName("description")
        val description: String?,
        @SerialName("eventList")
        val eventList: List<String>?,
        @SerialName("id")
        val id: Int,
        @SerialName("image")
        val image: String?,
        @SerialName("mbti")
        val mbti: String?,
        @SerialName("name")
        val name: String,
        @SerialName("phoneNumber")
        val phoneNumber: String?,
        @SerialName("userID")
        val userID: Int
    )
}