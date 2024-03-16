package com.lee.remember.model

import com.lee.remember.local.model.UserRealm

data class User(
    var id: Int = -1,
    var email: String = "",
    var name: String = "",
    var phoneNumber: String = "",
    var profileImage: String = "",
    var isLocalMode: Boolean = true,
)


fun UserRealm.asDomain() =
    User(
        id = userId,
        email = email,
        name = name,
        phoneNumber = name,
        profileImage = profileImage,
        isLocalMode = isLocalMode
    )
