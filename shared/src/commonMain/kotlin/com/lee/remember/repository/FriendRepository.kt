package com.lee.remember.repository

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.FriendDao
import com.lee.remember.local.model.asRealm
import com.lee.remember.remote.FriendApi
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class FriendRepository(
    val friendDao: FriendDao = FriendDao(),
    val friendApi: FriendApi = FriendApi(),
    val authDao: AuthDao = AuthDao(),
) {
    private val token = authDao.getToken() ?: ""

    suspend fun fetchFriends() = withContext(Dispatchers.IO) {
        val result = friendApi.getFriendList(token)
        result?.let { response ->
            val friends = response.result?.map { it.asRealm() }?.toRealmList() ?: realmListOf()
            friendDao.setFriends(friends)
        }
    }

}