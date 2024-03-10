package com.lee.remember.local.model

import com.lee.remember.model.Friend
import com.lee.remember.remote.request.FriendResponse
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class FriendRealm : RealmObject {
    @PrimaryKey
    var id: Int = -1
    var name: String = ""
    var phoneNumber: String = ""
    var group: String = ""
    var events: RealmList<EventRealm> = realmListOf()
    var profileImage: ProfileImageRealm? = null

    var isSynced: Boolean = false
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


fun FriendRealm.asDomain() =
    Friend(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        image = profileImage?.image ?: "",
        grouped = group,
        birthDate = events.firstOrNull()?.date ?: ""
    )

fun FriendResponse.Result.asRealm() =
    FriendRealm().apply {
        id = this@asRealm.id
        name = this@asRealm.name
        phoneNumber = this@asRealm.phoneNumber ?: ""
        events = this@asRealm.events?.map { it.asRealm() }?.toRealmList() ?: realmListOf()
        profileImage = this@asRealm.profileImage?.asRealm()

        isSynced = true
    }

fun FriendResponse.Result.Event.asRealm() =
    EventRealm().apply {
        id = this@asRealm.id
        name = this@asRealm.name
        date = this@asRealm.date
    }

fun FriendResponse.Result.ProfileImage.asRealm() =
    ProfileImageRealm().apply {
        id = this@asRealm.id
        image = this@asRealm.image
    }