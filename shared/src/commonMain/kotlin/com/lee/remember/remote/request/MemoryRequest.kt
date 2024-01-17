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
    @SerialName("friendIds")
    val friendIds: List<Int>,
    @SerialName("images")
    val images: List<Image>,
) {
    @Serializable
    data class Image(
        @SerialName("image")
        val image: String,
    )
}

@Serializable
data class MemoryAddResponse(
    @SerialName("result")
    val result: Result?,
    @SerialName("resultCode")
    val resultCode: String,
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
        @SerialName("images")
        val images: List<Image>?,
        @SerialName("friends")
        val friends: List<Friend>?,
    ) {
        @Serializable
        data class Image(
            @SerialName("id")
            val id: Int,
            @SerialName("image")
            val image: String?,
        )

        @Serializable
        data class Friend(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String?,
            @SerialName("profileImage")
            val profileImage: ProfileImage?,
        ) {
            @Serializable
            data class ProfileImage(
                @SerialName("id")
                val id: Int,
                @SerialName("image")
                val image: String?,
            )
        }
    }
}

@Serializable
data class MemoryGetResponse(
    @SerialName("result")
    val result: Result?,
    @SerialName("resultCode")
    val resultCode: String,
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
        val friends: List<Friend> = listOf(),

        @SerialName("images")
        val images: List<Image>? = listOf(),
    ) {
        @Serializable
        data class Image(
            @SerialName("id")
            val id: Int,
            @SerialName("image")
            val image: String?,
        )

        @Serializable
        data class Friend(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String,
            @SerialName("profileImage")
            val profileImage: ProfileImage?,
        ) {
            @Serializable
            data class ProfileImage(
                @SerialName("id")
                val id: Int,
                @SerialName("image")
                val image: String?,
            )
        }
    }
}

@Serializable
data class MemoryGetListResponse(
    @SerialName("resultCode")
    val resultCode: String,
    @SerialName("result")
    val result: List<Result>? = listOf(),
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
        val friends: List<Friend> = listOf(),

        @SerialName("thumbnail")
        val thumbnail: Thumbnail?,
    ) {
        @Serializable
        data class Thumbnail(
            @SerialName("id")
            val id: Int,
            @SerialName("image")
            val image: String?,
        )

        @Serializable
        data class Friend(
            @SerialName("id")
            val id: Int,
            @SerialName("name")
            val name: String,
        )
    }
}