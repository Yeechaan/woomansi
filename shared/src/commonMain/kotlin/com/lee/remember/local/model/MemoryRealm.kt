package com.lee.remember.local.model

import com.lee.remember.remote.request.MemoryGetListResponse
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class MemoryRealm : RealmObject {
    @PrimaryKey
    var id: Int = -1
    var title: String = ""
    var description: String = ""
    var date: String = ""
    var images: RealmList<String> = realmListOf()
    var friendTags: RealmList<FriendTagRealm> = realmListOf()
}

class FriendTagRealm : EmbeddedRealmObject {
    var id: Int = -1
    var name: String = ""
}


fun MemoryGetListResponse.Result.asRealm() =
    MemoryRealm().apply {
        id = this@asRealm.id
        title = this@asRealm.title ?: ""
        description = this@asRealm.description ?: ""
        date = this@asRealm.date ?: ""
        images = realmListOf(this@asRealm.thumbnail?.image ?: "")
        friendTags = this@asRealm.friends.map { it.asRealm() }.toRealmList()
    }

fun MemoryGetListResponse.Result.Friend.asRealm() =
    FriendTagRealm().apply {
        id = this@asRealm.id
        name = this@asRealm.name
    }