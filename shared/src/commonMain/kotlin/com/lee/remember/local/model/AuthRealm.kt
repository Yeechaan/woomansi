package com.lee.remember.local.model

import io.realm.kotlin.types.RealmObject

class AuthRealm : RealmObject {
    var accessToken: String = ""
}