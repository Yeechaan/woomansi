package com.lee.remember.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/*
* 이미지 변경 시 Image의 id 필드 없이 새로운 image 만 전송
* */
@Serializable
data class MemoryUpdateRequest(
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
        @SerialName("id")
        val id: Int = -1,
        @SerialName("image")
        val image: String = ""
    )
}

@Serializable
data class MemoryUpdateResponse(
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
            val id: Int,
            @SerialName("name")
            val name: String,
            @SerialName("profileImage")
            val profileImage: ProfileImage?
        ) {
            @Serializable
            data class ProfileImage(
                @SerialName("id")
                val id: Int,
                @SerialName("image")
                val image: String?,
            )
        }

        @Serializable
        data class Image(
            @SerialName("id")
            val id: Int,
            @SerialName("image")
            val image: String
        )
    }
}