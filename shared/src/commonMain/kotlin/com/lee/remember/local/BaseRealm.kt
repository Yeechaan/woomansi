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
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.first

class BaseRealm(
    private val encryptionRealm: EncryptionRealm,
    private val realmDataStore: RealmDataStore,
) {
    lateinit var realm: Realm

    suspend fun initRealm() {
        val realmSettings = realmDataStore.settings.first()

        val encryptionKey = getEncryptionKey(realmSettings)
        openRealm(encryptionKey)
    }

    private fun openRealm(encryptionKey: ByteArray) {
        val configuration = RealmConfiguration.Builder(
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
            encryptionKey(encryptionKey)
        }.build()

        // realmKey 사용 후 메모리 초기화
        encryptionKey.fill(0)

        realm = Realm.open(configuration)
    }

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

    private suspend fun getEncryptionKey(settings: RealmSettings): ByteArray {
        if (!encryptionRealm.isKeyExist() || settings.encryptedKey.isEmpty()) {
            val (iv, encryptedKey) = encryptionRealm.createEncryptedRealmKey()
            settings.initialVector = iv
            settings.encryptedKey = encryptedKey

            realmDataStore.saveSettings(iv, encryptedKey)
        }

        return encryptionRealm.getKey(settings.initialVector, settings.encryptedKey)
    }
}