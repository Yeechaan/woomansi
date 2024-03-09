package com.lee.remember.model

data class Friend(
    val id: Int = 0,
    val name: String = "",
    val phoneNumber: String = "",
    var description: String = "",
    var mbti: String = "",
    var image: String = "",

    var grouped: String = "",
    var birthDate: String = "",
)