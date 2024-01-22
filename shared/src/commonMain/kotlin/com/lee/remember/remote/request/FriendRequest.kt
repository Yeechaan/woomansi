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
    @SerialName("profileImage")
    val profileImage: String = "",
    @SerialName("events")
    val events: List<Event> = listOf(),
    @SerialName("friendGroups")
    val friendGroups: List<Event> = listOf(),
) {
    @Serializable
    data class Event(
        @SerialName("name")
        val name: String,
        @SerialName("date")
        val date: String
    )

    @Serializable
    data class FriendGroup(
        @SerialName("id")
        val id: Int
    )
}

@Serializable
data class FriendAddResponse(
    @SerialName("resultCode")
    val resultCode: String,
    @SerialName("result")
    val result: List<FriendResponse.Result>?
)
{
//    @Serializable
//    data class Result(
//        @SerialName("id")
//        val id: Int,
//        @SerialName("name")
//        val name: String,
//        @SerialName("phoneNumber")
//        val phoneNumber: String,
//        @SerialName("description")
//        val description: String?,
//        @SerialName("events")
//        val events: List<Event>?,
//        @SerialName("friendGroups")
//        val friendGroups: List<String>?,
//        @SerialName("profileImage")
//        val profileImage: ProfileImage?,
//    ) {
//        @Serializable
//        data class Event(
//            @SerialName("id")
//            val id: Int,
//            @SerialName("name")
//            val name: String,
//            @SerialName("date")
//            val date: String,
//        )
//
//        @Serializable
//        data class ProfileImage(
//            @SerialName("id")
//            val id: Int,
//            @SerialName("image")
//            val image: String
//        )
//    }
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
        @SerialName("friendGroups")
        val friendGroups: List<FriendGroup>?,
        @SerialName("profileImage")
        val profileImage: ProfileImage?,
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

        @Serializable
        data class FriendGroup(
            @SerialName("id")
            val id: Int,
            @SerialName("title")
            val title: String,
            @SerialName("description")
            val description: String,
            @SerialName("image")
            val image: Image,
        ) {
            @Serializable
            data class Image(
                @SerialName("id")
                val id: Int,
                @SerialName("image")
                val image: String
            )
        }
    }
}

@Serializable
data class FriendDetailResponse(
    @SerialName("resultCode")
    val resultCode: String,
    @SerialName("result")
    val result: FriendResponse.Result?
)