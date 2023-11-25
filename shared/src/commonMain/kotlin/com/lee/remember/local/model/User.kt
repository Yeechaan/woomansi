package com.lee.remember.local.model

import io.realm.kotlin.types.RealmObject

class User : RealmObject {
    var email: String = ""
    var password: String = ""
    var userId: Int = -1
    var name: String = ""
}