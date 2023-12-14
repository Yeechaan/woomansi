package com.lee.remember.local.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject

class FriendRealm : RealmObject {
    var id: Int = -1
    var name: String = ""
    var phoneNumber: String = ""
    var group: String = ""
    var events: RealmList<EventRealm> = realmListOf()
    var profileImage: ProfileImageRealm? = null
}

class EventRealm : RealmObject {
    var id: Int = -1
    var name: String = ""
    var date: String = ""
}

class ProfileImageRealm : RealmObject {
    var id: Int = -1
    var image: String = ""
}