package com.lee.remember.local.dao

import com.lee.remember.local.BaseRealm
import com.lee.remember.local.model.EventRealm
import com.lee.remember.local.model.FriendRealm
import com.lee.remember.local.model.ProfileImageRealm
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmList

class FriendDao {
    private val realm = BaseRealm.realm

    suspend fun setFriend(friend: FriendRealm) {
        realm.write {
            copyToRealm(friend)
        }
    }

    suspend fun setFriends(friends: RealmList<FriendRealm>) {
        realm.write {
            friends.forEach {
                copyToRealm(it)
            }
        }
    }

    suspend fun updateFriend(friend: FriendRealm) {
        realm.write {
            val liveFriend = query<FriendRealm>("id == ${friend.id}").find().first()
            liveFriend.apply {
                this.name = friend.name
                this.phoneNumber = friend.phoneNumber
                this.group = friend.group
                this.events.add(EventRealm().apply { this.name = friend.events.first().name; this.date = friend.events.first().date })
                this.profileImage = ProfileImageRealm().apply { this.image = friend.profileImage?.image ?: "" }
            }
        }
    }

}