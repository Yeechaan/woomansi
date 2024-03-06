package com.lee.remember.local.dao

import com.lee.remember.local.BaseRealm
import com.lee.remember.local.model.AuthRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

class AuthDao(
    private val baseRealm: BaseRealm,
) {
    private val realm: Realm
        get() = baseRealm.realm

    suspend fun updateAuth(token: String, password: String) {
        realm.write {
            val authRealm = query<AuthRealm>().find().firstOrNull()
            if (authRealm != null) {
                authRealm.accessToken = "Bearer $token"
                authRealm.password = password
            } else {
                copyToRealm(AuthRealm().apply {
                    this.accessToken = "Bearer $token"
                    this.password = password
                })
            }
        }
    }

    fun getToken() = realm.query<AuthRealm>().find().firstOrNull()?.accessToken
    fun getAuth() = realm.query<AuthRealm>().find().firstOrNull()

    suspend fun delete() {
        realm.write {
            val authRealm = this.query<AuthRealm>().find()
            delete(authRealm)
        }
    }
}