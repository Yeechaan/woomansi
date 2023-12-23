package com.lee.remember.local.dao

import com.lee.remember.local.BaseRealm
import com.lee.remember.local.model.FriendRealm
import com.lee.remember.local.model.MemoryRealm
import io.realm.kotlin.ext.query

class MemoryDao {
    private val realm = BaseRealm.realm

    // 친구 정보 from remote(friendId != -1)
    suspend fun setMemoryByFriendId(friendId: Int, memory: MemoryRealm) {
        realm.write {
            val friend = query<FriendRealm>("id==$friendId").find().firstOrNull()
            friend?.memories?.add(memory)
        }
    }

    // 친구 정보 from local(friendId == -1)
    suspend fun setMemoryByPhoneNumber(phoneNumber: String, memory: MemoryRealm) {
        realm.write {
            val friend = query<FriendRealm>("phoneNumber=='$phoneNumber'").find().firstOrNull()
            friend?.memories?.add(memory)
        }
    }

    fun getAllMemories() = realm.query<MemoryRealm>().find()
}