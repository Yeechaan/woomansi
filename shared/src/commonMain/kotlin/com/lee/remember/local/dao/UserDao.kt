package com.lee.remember.local.dao

import com.lee.remember.local.BaseRealm
import com.lee.remember.local.model.UserRealm
import io.realm.kotlin.ext.query

class UserDao {
    private val realm = BaseRealm.realm

    suspend fun setUser(user: UserRealm) {
        realm.write {
            copyToRealm(user)
        }
    }

    // Todo flow 사용 여부 검토
    fun getUser() = realm.query<UserRealm>().find().firstOrNull()
}