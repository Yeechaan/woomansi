package com.lee.remember.local

import com.lee.remember.encryption.EncryptionRealm
import com.lee.remember.local.datastore.RealmDataStore
import com.lee.remember.local.datastore.RealmSettings
import com.lee.remember.local.model.AuthRealm
import com.lee.remember.local.model.EventRealm
import com.lee.remember.local.model.FriendRealm
import com.lee.remember.local.model.MemoryFriendRealm
import com.lee.remember.local.model.MemoryRealm
import com.lee.remember.local.model.ProfileImageRealm
import com.lee.remember.local.model.UserRealm
import io.github.aakira.napier.Napier
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BaseRealm(
    private val encryptionRealm: EncryptionRealm,
    private val realmDataStore: RealmDataStore,
) {

    private val coroutineScope = CoroutineScope(Job())
    private lateinit var settings: RealmSettings

    init {
        coroutineScope.launch {
            realmDataStore.settings.collectLatest {
                Napier.d("### $it")
                settings = it
            }
        }
    }

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
        encryptionKey(getEncryptionKey())
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

    private fun getEncryptionKey(): ByteArray {
        if (!encryptionRealm.isKeyExist()) {
            val (iv, encryptedKey) = encryptionRealm.createEncryptedRealmKey()

            coroutineScope.launch {
                realmDataStore.saveSettings(iv, encryptedKey)
                Napier.d("### $iv, $encryptedKey")
            }
        }

        return encryptionRealm.getKey(settings.initialVector, settings.encryptedKey)
    }
}