package com.lee.remember.local.dao

import com.lee.remember.local.BaseRealm
import com.lee.remember.local.model.AuthRealm
import io.realm.kotlin.ext.query

class AuthDao {
    private val realm = BaseRealm.realm

    suspend fun updateToken(token: String) {
        realm.write {
            val authRealm = query<AuthRealm>().find().firstOrNull()
            if (authRealm != null) {
                authRealm.accessToken = token
            } else {
                copyToRealm(AuthRealm().apply { this.accessToken = token })
            }
        }
    }

    fun getToken() = realm.query<AuthRealm>().find().firstOrNull()
}