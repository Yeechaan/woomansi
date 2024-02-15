package com.lee.remember.repository

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.FriendDao
import com.lee.remember.local.model.EventRealm
import com.lee.remember.local.model.FriendRealm
import com.lee.remember.local.model.ProfileImageRealm
import com.lee.remember.local.model.asRealm
import com.lee.remember.remote.FriendApi
import com.lee.remember.remote.request.FriendRequest
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class FriendRepository(
    private val friendDao: FriendDao,
    private val friendApi: FriendApi,
    private val authDao: AuthDao,
) {
    private val token = authDao.getToken() ?: ""

    fun getFriendsAsFlow() = friendDao.getFriends().asFlow()
    fun getFriendAsFlow(friendId: Int) = friendDao.getFriend(friendId)?.asFlow()

    fun getFriends() = friendDao.getFriends()
    fun getFriend(friendId: Int) = friendDao.getFriend(friendId)

    suspend fun addFriends(friends: List<FriendRequest>): Result<Boolean> =
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

    suspend fun updateFriend(friendId: String, friend: FriendRequest): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val result = friendApi.updateFriend(token, friendId, friend)
            result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS" && it.result != null) {
                        val friendRealm = it.result.asRealm()
                        friendDao.updateFriend(friendRealm)
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


    // 네트워크에 연결되지 않은 경우 로컬 DB에만 저장한다.
    suspend fun addFriendsToLocal(friends: List<FriendRequest>): Result<Boolean> =
        withContext(Dispatchers.IO) {
            var savedFriendId = friendDao.getFriends().size

            val friendsRealm = friends.map {
                val eventRealm = it.events.firstOrNull()?.let {
                    realmListOf(EventRealm().apply {
                        name = it.name
                        date = it.date
                    })
                } ?: realmListOf()

                FriendRealm().apply {
                    id = savedFriendId++
                    name = it.name
                    phoneNumber = it.phoneNumber ?: ""
                    events = eventRealm
                    profileImage = ProfileImageRealm().apply { image = it.profileImage }

                    isSynced = false
                }
            }.toRealmList()

            friendDao.setFriends(friendsRealm)

            Result.success(true)
        }

    suspend fun updateFriendToLocal(friendId: String, friend: FriendRequest): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val eventRealm = friend.events.firstOrNull()?.let {
                realmListOf(EventRealm().apply {
                    name = it.name
                    date = it.date
                })
            } ?: realmListOf()

            val friendRealm = FriendRealm().apply {
                id = friendId.toInt()
                name = friend.name
                phoneNumber = friend.phoneNumber ?: ""
                events = eventRealm
                profileImage = ProfileImageRealm().apply { image = friend.profileImage }

                isSynced = false
            }

            friendDao.updateFriend(friendRealm)

            Result.success(true)
        }
}