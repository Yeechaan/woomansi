package com.lee.remember.repository

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.remote.AuthApi
import com.lee.remember.remote.request.LoginRequest
import com.lee.remember.remote.request.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class AuthRepository(
    val authApi: AuthApi = AuthApi(),
    val authDao: AuthDao = AuthDao(),
) {

    fun getToken() = authDao.getToken()

    suspend fun login(email: String, password: String): Result<LoginResponse> =
        withContext(Dispatchers.IO) {
            val request = LoginRequest(email = email, password = password)
            val result = authApi.login(request)

            return@withContext result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        authDao.updateToken(it.result?.jwtToken ?: "")
                        Result.success(it)
                    } else {
                        Result.failure(Exception(it.resultCode))
                    }
                },
                onFailure = {
                    Result.failure(Exception(it))
                }
            )
        }

}