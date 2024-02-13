package com.lee.remember.local.dao

import com.lee.remember.local.BaseRealm
import com.lee.remember.local.model.FriendRealm
import com.lee.remember.local.model.UserRealm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query

class UserDao(
    private val baseRealm: BaseRealm,
) {
    private val realm = baseRealm.realm

    suspend fun setUser(user: UserRealm) {
        realm.write {
            copyToRealm(user, UpdatePolicy.ALL)
        }
    }

    suspend fun deleteUser() {
        realm.write {
            val userToDelete: UserRealm? = query<UserRealm>().find().firstOrNull()
            userToDelete?.let { delete(it) }
        }
    }

    // Todo flow 사용 여부 검토
    fun getUser() = realm.query<UserRealm>().find().firstOrNull()

    suspend fun delete() {
        realm.write {
            val userRealm = this.query<UserRealm>().find()
            delete(userRealm)
        }
    }
}