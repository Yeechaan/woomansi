package com.lee.remember.local.model

import com.lee.remember.remote.request.SignupResponse
import com.lee.remember.remote.request.UserInfoResponse
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class UserRealm : RealmObject {
    @PrimaryKey
    var userId: Int = -1
    var email: String = ""
    var name: String = ""
    var phoneNumber: String = ""
    var profileImage: String = ""

    var isLocalMode: Boolean = true
}

fun SignupResponse.asRealm() =
    UserRealm().apply {
        this.userId = result?.id ?: -1
        this.email = result?.email ?: ""
        this.name = result?.name ?: ""
        this.phoneNumber = result?.phoneNumber ?: ""
        this.profileImage = result?.profileImage?.image ?: ""

        this.isLocalMode = false
    }

fun UserInfoResponse.asRealm() =
    UserRealm().apply {
        this.userId = result?.id ?: -1
        this.email = result?.email ?: ""
        this.name = result?.name ?: ""
        this.phoneNumber = result?.phoneNumber ?: ""
        this.profileImage = result?.profileImage?.image ?: ""

        this.isLocalMode = false
    }