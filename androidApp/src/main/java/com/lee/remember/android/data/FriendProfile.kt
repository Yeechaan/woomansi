package com.lee.remember.android.data

import android.net.Uri

data class FriendProfile(
    val name: String = "",
    val phoneNumber: String = "",
    var description: String = "",
    var mbti: String = "",
    var image: String = "",

    var grouped: String = "",
    var birthDate: String = "",

    val history: MutableList<FriendHistory> = mutableListOf<FriendHistory>()
)

data class FriendHistory(
    var title: String = "",
    var contents: String = "",
    var imageUri: Uri? = null,
)
