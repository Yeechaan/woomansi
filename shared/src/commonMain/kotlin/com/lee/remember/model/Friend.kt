package com.lee.remember.model

import com.lee.remember.local.model.EventRealm
import com.lee.remember.local.model.FriendRealm

data class Friend(
    val id: Int = 0,
    val name: String = "",
    val phoneNumber: String = "",
    var image: String = "",
    var events: List<Event> = emptyList(),

    var description: String = "",
    var mbti: String = "",
    var grouped: String = "",
    var birthDate: String = "",
)

data class Event(
    var name: String = "",
    var date: String = "",
)


fun FriendRealm.asDomain() =
    Friend(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        image = profileImage?.image ?: "",
        events = events.map { it.asDomain() },

        grouped = group,
        birthDate = events.firstOrNull()?.date ?: ""
    )

fun EventRealm.asDomain() =
    Event(
        name = name,
        date = date,
    )