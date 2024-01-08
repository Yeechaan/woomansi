package com.lee.remember.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemoryRequest(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("date")
    val date: String,
    @SerialName("friends")
    val friends: List<Friend>,
    @SerialName("images")
    val images: List<Image>,
) {
    @Serializable
    data class Friend(
        @SerialName("id")
        val id: Int
    )

    @Serializable
    data class Image(
        @SerialName("image")
        val image: String
    )
}

@Serializable
data class MemoryAddResponse(
    @SerialName("result")
    val result: Result?,
    @SerialName("resultCode")
    val resultCode: String
) {
    @Serializable
    data class Result(
        @SerialName("id")
        val id: Int,
        @SerialName("title")
        val title: String?,
        @SerialName("description")
        val description: String?,
        @SerialName("date")
        val date: String?,
        @SerialName("friends")
        val friends: List<Friend>?,
        @SerialName("images")
        val images: List<Image>?,
    ) {
        @Serializable
        data class Friend(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String?,
            @SerialName("profileImage")
            val profileImage: ProfileImage?
        ) {
            @Serializable
            data class ProfileImage(
                @SerialName("id")
                val id: Int,
                @SerialName("image")
                val image: String?
            )
        }

        @Serializable
        data class Image(
            @SerialName("id")
            val id: Int,
            @SerialName("image")
            val image: String?
        )
    }
}