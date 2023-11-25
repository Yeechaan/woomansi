package com.lee.remember.local.dao

import com.lee.remember.local.model.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class UserDao {

    // use the RealmConfiguration.Builder() for more options
    private val configuration = RealmConfiguration.create(schema = setOf(User::class))
    private val realm = Realm.open(configuration)

    suspend fun setUser(user: User) {
        realm.write {
            copyToRealm(user)
        }
    }

    // flow 사용 여부
    fun getUser() = realm.query<User>().find().firstOrNull()
}