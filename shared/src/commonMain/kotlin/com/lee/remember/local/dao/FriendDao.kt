package com.lee.remember.local.dao

import com.lee.remember.local.BaseRealm
import com.lee.remember.local.model.AuthRealm
import com.lee.remember.local.model.EventRealm
import com.lee.remember.local.model.FriendRealm
import com.lee.remember.local.model.ProfileImageRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.RealmList

class FriendDao(
    private val baseRealm: BaseRealm,
) {
    //    private val realm = baseRealm.realm
    private val realm: Realm
        get() = baseRealm.realm

    suspend fun setFriend(friend: FriendRealm) {
        realm.write {
            copyToRealm(friend)
        }
    }

    suspend fun setFriends(friends: RealmList<FriendRealm>) {
        realm.write {
            friends.forEach {
                copyToRealm(it, UpdatePolicy.ALL)
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
                this.events = friend.events
                this.profileImage = friend.profileImage
            }
        }
    }

    fun getFriends() = realm.query<FriendRealm>().find()
    fun getFriend(friendId: Int) = realm.query<FriendRealm>("id==$friendId").find().firstOrNull()

    suspend fun delete() {
        realm.write {
            val friendRealm = this.query<FriendRealm>().find()
            delete(friendRealm)
        }
    }
}