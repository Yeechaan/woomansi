package com.lee.remember.local.dao

import com.lee.remember.local.BaseRealm
import com.lee.remember.local.model.FriendRealm
import com.lee.remember.local.model.MemoryRealm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.flow.asFlow

class MemoryDao {
    private val realm = BaseRealm.realm

    suspend fun setMemories(memories: RealmList<MemoryRealm>) {
        realm.write {
            memories.forEach {
                copyToRealm(it, UpdatePolicy.ALL)
            }
        }
    }

    suspend fun deleteMemory(memoryId: Int) {
        realm.write {
            val memory = query<MemoryRealm>("id == $memoryId")
            delete(memory)
        }
    }

    fun getMemory(id: Int) = realm.query<MemoryRealm>("id==$id").find()
    fun getMemories() = realm.query<MemoryRealm>().find()
}