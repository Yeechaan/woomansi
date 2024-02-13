package com.lee.remember.local

import com.lee.remember.local.model.AuthRealm
import com.lee.remember.local.model.EventRealm
import com.lee.remember.local.model.FriendRealm
import com.lee.remember.local.model.MemoryFriendRealm
import com.lee.remember.local.model.MemoryRealm
import com.lee.remember.local.model.ProfileImageRealm
import com.lee.remember.local.model.UserRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.dynamic.DynamicMutableRealm
import io.realm.kotlin.dynamic.DynamicRealm
import io.realm.kotlin.ext.query
import io.realm.kotlin.migration.AutomaticSchemaMigration
import io.realm.kotlin.migration.RealmMigration

class BaseRealm {

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
        schemaVersion(2)
//        deleteRealmIfMigrationNeeded()
    }.migration(AutomaticSchemaMigration {
//        val oldRealm = it.oldRealm
//        val newRealm = it.newRealm
//
//        it.enumerate("UserRealm") { oldObject, newObject ->
//            newObject?.set("test", "hi")
//        }
    }).build()

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
//        realm.close()
//        Realm.deleteRealm(configuration)
    }
}

class RememberMigration(): AutomaticSchemaMigration.MigrationContext {
    override val newRealm: DynamicMutableRealm
        get() = TODO("Not yet implemented")
    override val oldRealm: DynamicRealm
        get() = TODO("Not yet implemented")

}