package com.lee.remember.local

import com.lee.remember.Platform
import com.lee.remember.getPlatform
import com.lee.remember.local.model.AuthRealm
import com.lee.remember.local.model.EventRealm
import com.lee.remember.local.model.FriendRealm
import com.lee.remember.local.model.MemoryFriendRealm
import com.lee.remember.local.model.MemoryRealm
import com.lee.remember.local.model.ProfileImageRealm
import com.lee.remember.local.model.UserRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class BaseRealm {
    private val platform: Platform = getPlatform()

    private val configuration = RealmConfiguration.Builder(
        setOf(
            UserRealm::class,
            FriendRealm::class,
            EventRealm::class,
            ProfileImageRealm::class,
            MemoryRealm::class,
            AuthRealm::class,
            MemoryFriendRealm::class
        )
    ).apply {
//        schemaVersion(1)
        deleteRealmIfMigrationNeeded()
        encryptionKey(platform.getSecretKey())
    }.build()

    val realm = Realm.open(configuration)

    suspend fun delete() {
        realm.write {
            val userRealm = this.query<UserRealm>().find()
            delete(userRealm)

            val authRealm = this.query<AuthRealm>().find()
            delete(authRealm)

            val friendRealm = this.query<FriendRealm>().find()
            delete(friendRealm)

            val memoryRealm = this.query<MemoryRealm>().find()
            delete(memoryRealm)
        }
    }
}