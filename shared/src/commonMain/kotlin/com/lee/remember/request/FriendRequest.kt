package com.lee.remember.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendListResponse(
    @SerialName("result")
    val result: List<FriendSummaryInfo>,
    @SerialName("resultCode")
    val resultCode: String
) {
    @Serializable
    data class FriendSummaryInfo(
        val id: Int,
        val name: String,
        val phoneNumber: String?,
        val image: String?,
    )
}
