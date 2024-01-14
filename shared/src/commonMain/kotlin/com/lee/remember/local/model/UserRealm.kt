package com.lee.remember.local.model

import com.lee.remember.remote.request.SignupResponse
import com.lee.remember.remote.request.UserInfoResponse
import io.realm.kotlin.types.RealmObject

class UserRealm : RealmObject {
    var userId: Int = -1
    var email: String = ""
    var name: String = ""
    var phoneNumber: String = ""
    var profileImage: String = ""
}

fun SignupResponse.asRealm() =
    UserRealm().apply {
        this.userId = result?.id ?: -1
        this.email = result?.email ?: ""
        this.name = result?.name ?: ""
        this.phoneNumber = result?.phoneNumber ?: ""
        this.profileImage = result?.profileImage?.image ?: ""
    }

fun UserInfoResponse.asRealm() =
    UserRealm().apply {
        this.userId = result?.id ?: -1
        this.email = result?.email ?: ""
        this.name = result?.name ?: ""
        this.phoneNumber = result?.phoneNumber ?: ""
        this.profileImage = result?.profileImage?.image ?: ""
    }