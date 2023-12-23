package com.lee.remember.local.model

import io.realm.kotlin.ext.backlinks
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList

class MemoryRealm : EmbeddedRealmObject {
    var id: Int = -1
    var title: String = ""
    var description: String = ""
    var date: String = ""
    var images: RealmList<String> = realmListOf()
    var friendTags: RealmList<String> = realmListOf()

    val friend: FriendRealm by backlinks(FriendRealm::memories)
}