package com.lee.remember.repository

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.FriendDao
import com.lee.remember.local.model.asRealm
import com.lee.remember.remote.FriendApi
import com.lee.remember.remote.request.FriendRequest
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

    fun getFriends() = friendDao.getFriends()
    fun getFriend(friendId: Int) = friendDao.getFriend(friendId)

    suspend fun addFriend(friends: List<FriendRequest>): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val result = friendApi.addFriends(token, friends)
            result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS" && it.result != null) {
                        val friendsRealm = it.result.map { it.asRealm() }.toRealmList()
                        friendDao.setFriends(friendsRealm)
                        Result.success(true)
                    } else {
                        Result.failure(Exception(it.resultCode))
                    }
                },
                onFailure = {
                    Result.failure(Exception(it))
                }
            )
        }

    suspend fun fetchFriends() = withContext(Dispatchers.IO) {
        val result = friendApi.getFriendList(token)
        result?.let { response ->
            val friends = response.result?.map { it.asRealm() }?.toRealmList() ?: realmListOf()
            friendDao.setFriends(friends)
        }
    }
}