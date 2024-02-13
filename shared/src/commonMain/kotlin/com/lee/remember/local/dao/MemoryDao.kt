package com.lee.remember.local.dao

import com.lee.remember.local.BaseRealm
import com.lee.remember.local.model.MemoryRealm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmList

class MemoryDao(
    private val baseRealm: BaseRealm,
) {
    private val realm = baseRealm.realm

    suspend fun setMemory(memory: MemoryRealm) {
        realm.write {
            copyToRealm(memory, UpdatePolicy.ALL)
        }
    }

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

    suspend fun updateMemory(memoryId: Int, updatedMemory: MemoryRealm) {
        realm.write {
            val memory = query<MemoryRealm>("id == $memoryId").find().first()
            memory.apply {
                title = updatedMemory.title
                description = updatedMemory.description
                date = updatedMemory.date
                images = updatedMemory.images
                friends = updatedMemory.friends
            }
        }
    }

    fun getMemory(id: Int) = realm.query<MemoryRealm>("id==$id").find()
    fun getMemories() = realm.query<MemoryRealm>().find()
}