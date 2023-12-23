package com.lee.remember.local.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject

class FriendRealm : RealmObject {
    var id: Int = -1
    var name: String = ""
    var phoneNumber: String = ""
    var group: String = ""
    var events: RealmList<EventRealm> = realmListOf()
    var profileImage: ProfileImageRealm? = null
    var memories: RealmList<MemoryRealm> = realmListOf()
}

class EventRealm : EmbeddedRealmObject {
    var id: Int = -1
    var name: String = ""
    var date: String = ""
}

class ProfileImageRealm : EmbeddedRealmObject {
    var id: Int = -1
    var image: String = ""
}