package com.lee.remember.remote

import com.lee.remember.request.FriendAddRequest
import com.lee.remember.request.FriendAddResponse
import com.lee.remember.request.FriendListResponse
import com.lee.remember.request.FriendMultiAddRequest
import com.lee.remember.request.FriendMultiAddResponse
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.EmptyContent.contentType
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

    val friendUrl = "http://101.101.216.129:8080/api/v1/friends/"


    suspend fun addFriend(token: String, request: FriendAddRequest): FriendAddResponse? {
        val response = client.post(friendUrl + "add") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }

//        return if (response.status == HttpStatusCode.OK) response.body() else null
        return response.body()
    }

    suspend fun addFriendMulti(token: String, request: FriendMultiAddRequest): FriendMultiAddResponse? {
        val response = client.post(friendUrl + "add-multi") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
            contentType(ContentType.Application.Json)
            setBody(request.friends)
        }

        return response.body()
    }

    suspend fun getFriendList(token: String): FriendListResponse? {
        val response = client.get(friendUrl + "list") {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
        }

        Napier.d("### ${response.bodyAsText()}")

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }

    // Todo response 정의
    suspend fun getFriend(token: String, friendId: Int): FriendListResponse? {
        val response = client.get(friendUrl + friendId) {
            headers {
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.Authorization, token)
            }
        }

        Napier.d("### ${response.bodyAsText()}")

        return if (response.status == HttpStatusCode.OK) response.body() else null
    }
}