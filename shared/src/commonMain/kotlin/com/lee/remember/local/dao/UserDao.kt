package com.lee.remember.local.dao

import com.lee.remember.local.model.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class UserDao {
    private val configuration = RealmConfiguration.Builder(setOf(User::class))
        .deleteRealmIfMigrationNeeded()
        .build()
    private val realm = Realm.open(configuration)

    suspend fun setUser(user: User) {
        realm.write {
            copyToRealm(user)
        }
    }

    // Todo flow 사용 여부 검토
    fun getUser() = realm.query<User>().find().firstOrNull()
}