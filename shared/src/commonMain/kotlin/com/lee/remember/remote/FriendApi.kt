package com.lee.remember.remote

import com.lee.remember.request.FriendAddResponse
import com.lee.remember.request.FriendDetailResponse
import com.lee.remember.request.FriendRequest
import com.lee.remember.request.FriendResponse
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class FriendApi {

    val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private val friendUrl = baseUrl + "friends"

    suspend fun addFriend(token: String, friends: List<FriendRequest>): FriendAddResponse? {
        val response = client.post(friendUrl) {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
            contentType(ContentType.Application.Json)
            setBody(friends)
        }

//        Napier.d("### ${response.bodyAsText()}")
        Napier.d("###addFriend ${response.status}")
        Napier.d("@@@addFriend ${response.bodyAsText().length}")

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }

    suspend fun getFriend(token: String, friendId: String): FriendDetailResponse? {
        val response = client.get("$friendUrl/$friendId") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
        }

//        Napier.d("### ${response.bodyAsText()}")
        Napier.d("###getFriend ${response.status}")

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }

    suspend fun getFriendList(token: String): FriendResponse? {
        val response = client.get(friendUrl) {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
        }

//        Napier.d("### ${response.bodyAsText()}")
        Napier.d("###getFriendList ${response.status}")
        Napier.d("@@@getFriend ${response.bodyAsText().length}")

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }

    suspend fun updateFriend(token: String, friendId: String, friend: FriendRequest): FriendDetailResponse? {
        val response = client.put("$friendUrl/$friendId") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
            contentType(ContentType.Application.Json)
            setBody(friend)
        }

//        Napier.d("### ${response.bodyAsText()}")
        Napier.d("###updateFriend ${response.status}")

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }

}