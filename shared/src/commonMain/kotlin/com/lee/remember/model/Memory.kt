package com.lee.remember.model

import com.lee.remember.local.model.MemoryFriendRealm
import com.lee.remember.local.model.MemoryRealm

data class Memory(
    var id: Int = -1,
    var title: String = "",
    var description: String = "",
    var date: String = "",
    var image: String = "",
    var ownerFriend: MemoryFriend? = null,
    var friendTags: List<MemoryFriend> = listOf(),
)

data class MemoryFriend(
    var id: Int = -1,
    var name: String = "",
)

fun MemoryRealm.asData() = run {
    val memoryFriends = friends.map { it.asData() }
    val friendTags = if (memoryFriends.isNotEmpty()) memoryFriends.subList(1, memoryFriends.size) else listOf()

    Memory(
        id = id,
        title = title,
        description = description,
        date = date,
        image = images.firstOrNull() ?: "",
        ownerFriend = memoryFriends.firstOrNull(),
        friendTags = friendTags
    )
}

fun MemoryFriendRealm.asData() =
    MemoryFriend(id = id, name = name)