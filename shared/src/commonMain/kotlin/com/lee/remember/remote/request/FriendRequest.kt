package com.lee.remember.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendRequest(
    @SerialName("name")
    val name: String,
    @SerialName("phoneNumber")
    val phoneNumber: String = "",
    @SerialName("description")
    val description: String = "",
    @SerialName("events")
    val events: List<Event> = listOf(),
    @SerialName("profileImage")
    val profileImage: String = ""
) {
    @Serializable
    data class Event(
        @SerialName("name")
        val name: String,
        @SerialName("date")
        val date: String
    )
}

@Serializable
data class FriendAddResponse(
    @SerialName("resultCode")
    val resultCode: String,
    @SerialName("result")
    val result: List<Result>?
) {
    @Serializable
    data class Result(
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("phoneNumber")
        val phoneNumber: String,
        @SerialName("description")
        val description: String?,
        @SerialName("events")
        val events: List<Event>?,
        @SerialName("profileImage")
        val profileImage: ProfileImage?,
        @SerialName("friendGroups")
        val friendGroups: List<String>?,
    ) {
        @Serializable
        data class Event(
            @SerialName("id")
            val id: Int,
            @SerialName("date")
            val date: String,
            @SerialName("name")
            val name: String
        )

        @Serializable
        data class ProfileImage(
            @SerialName("id")
            val id: Int,
            @SerialName("image")
            val image: String
        )
    }
}

@Serializable
data class FriendResponse(
    @SerialName("resultCode")
    val resultCode: String,
    @SerialName("result")
    val result: List<Result>?
) {
    @Serializable
    data class Result(
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("phoneNumber")
        val phoneNumber: String?,
        @SerialName("description")
        val description: String?,
        @SerialName("events")
        val events: List<Event>? = listOf(),
        @SerialName("profileImage")
        val profileImage: ProfileImage?,
        @SerialName("friendGroups")
        val friendGroups: List<String>?,
    ) {
        @Serializable
        data class Event(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String,
            @SerialName("date")
            val date: String
        )

        @Serializable
        data class ProfileImage(
            @SerialName("id")
            val id: Int,
            @SerialName("image")
            val image: String
        )
    }
}

@Serializable
data class FriendDetailResponse(
    @SerialName("resultCode")
    val resultCode: String,
    @SerialName("result")
    val result: FriendResponse.Result?
)