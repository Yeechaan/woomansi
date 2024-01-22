package com.lee.remember.local.model

import com.lee.remember.remote.request.MemoryAddResponse
import com.lee.remember.remote.request.MemoryGetListResponse
import com.lee.remember.remote.request.MemoryUpdateResponse
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
    var friends: RealmList<MemoryFriendRealm> = realmListOf()
}

class MemoryFriendRealm : EmbeddedRealmObject {
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
        friends = this@asRealm.friends.map { it.asRealm() }.toRealmList()
    }

fun MemoryGetListResponse.Result.Friend.asRealm() =
    MemoryFriendRealm().apply {
        id = this@asRealm.id
        name = this@asRealm.name
    }

fun MemoryAddResponse.Result.asRealm() =
    MemoryRealm().apply {
        id = this@asRealm.id
        title = this@asRealm.title ?: ""
        description = this@asRealm.description ?: ""
        date = this@asRealm.date ?: ""
        images = realmListOf(this@asRealm.images?.firstOrNull()?.image ?: "")
        friends = this@asRealm.friends?.map { it.asRealm() }?.toRealmList() ?: realmListOf()
    }

fun MemoryAddResponse.Result.Friend.asRealm() =
    MemoryFriendRealm().apply {
        id = this@asRealm.id
        name = this@asRealm.name ?: ""
    }

fun MemoryUpdateResponse.Result.asRealm() =
    MemoryRealm().apply {
        id = this@asRealm.id
        title = this@asRealm.title ?: ""
        description = this@asRealm.description ?: ""
        date = this@asRealm.date ?: ""
        images = realmListOf(this@asRealm.images.firstOrNull()?.image ?: "")
        friends = this@asRealm.friends.map { it.asRealm() }.toRealmList()
    }

fun MemoryUpdateResponse.Result.Friend.asRealm() =
    MemoryFriendRealm().apply {
        id = this@asRealm.id
        name = this@asRealm.name
    }