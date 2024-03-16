package com.lee.remember.repository

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.UserDao
import com.lee.remember.local.model.asRealm
import com.lee.remember.remote.AuthApi
import com.lee.remember.remote.request.EmailRequest
import com.lee.remember.remote.request.LoginRequest
import com.lee.remember.remote.request.SignupRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class AuthRepository(
    private val authApi: AuthApi,
    private val authDao: AuthDao,
    private val userDao: UserDao,
) {

    fun getToken() = authDao.getToken()

    suspend fun signUp(email: String, password: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val request = SignupRequest(email, password)
            val result = authApi.signup(request)

            return@withContext result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        userDao.setUser(it.asRealm())
                        authDao.updateAuth(it.result?.jwtToken ?: "")

                        Result.success(true)
                    } else {
                        Result.failure(Exception(it.resultCode))
                    }
                },
                onFailure = {
                    Result.failure(Exception(it.message))
                }
            )
        }

    suspend fun login(email: String, password: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val request = LoginRequest(email = email, password = password)
            val result = authApi.login(request)

            return@withContext result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        authDao.updateAuth(it.result?.jwtToken ?: "")
                        Result.success(true)
                    } else {
                        Result.failure(Exception(it.resultCode))
                    }
                },
                onFailure = {
                    Result.failure(Exception(it))
                }
            )
        }

    suspend fun sendEmailCode(email: String): Result<String> =
        withContext(Dispatchers.IO) {
            val request = EmailRequest(email)
            val result = authApi.sendEmailCode(request)

            return@withContext result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS" && it.result != null) {
                        Result.success(it.result.code)
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