package com.lee.remember.local

import com.lee.remember.local.model.EventRealm
import com.lee.remember.local.model.FriendRealm
import com.lee.remember.local.model.ProfileImageRealm
import com.lee.remember.local.model.UserRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object BaseRealm {

    private val configuration = RealmConfiguration.Builder(
        setOf(UserRealm::class, FriendRealm::class, EventRealm::class, ProfileImageRealm::class)
    ).apply {
        deleteRealmIfMigrationNeeded()
    }.build()

    val realm = Realm.open(configuration)
}