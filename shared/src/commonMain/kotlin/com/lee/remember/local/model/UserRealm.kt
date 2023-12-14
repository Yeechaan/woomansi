package com.lee.remember.local.model

import io.realm.kotlin.types.RealmObject

class UserRealm : RealmObject {
    var userId: Int = -1
    var email: String = ""
    var password: String = ""
    var name: String = ""
    var phoneNumber: String = ""
    var profileImage: String = ""
}