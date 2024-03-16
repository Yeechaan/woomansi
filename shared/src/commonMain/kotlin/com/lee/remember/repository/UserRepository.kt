package com.lee.remember.repository

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.UserDao
import com.lee.remember.local.model.UserRealm
import com.lee.remember.local.model.asRealm
import com.lee.remember.model.asDomain
import com.lee.remember.remote.UserApi
import com.lee.remember.remote.request.UserInfoRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class UserRepository(
    private val userDao: UserDao,
    private val userApi: UserApi,
    private val authDao: AuthDao,
) {
    private val token: String
        get() = authDao.getToken() ?: ""

    fun getUser() = userDao.getUser()?.asDomain()

    suspend fun updateUserName(name: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val user = getUser() ?: return@withContext Result.failure(Exception(""))
            val userInfoRequest = UserInfoRequest(user.email, name, user.phoneNumber, user.profileImage)
            val result = userApi.updateUser(token, userInfoRequest)

            return@withContext result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        val userRealm = it.asRealm()
                        userDao.deleteUser()
                        userDao.setUser(userRealm)

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

    suspend fun fetchUser(): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val result = userApi.getUser(token)

            return@withContext result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
                        val userRealm = it.asRealm()
                        userDao.setUser(userRealm)

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

    suspend fun deleteUser(): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val result = userApi.deleteUser(token)

            return@withContext result.fold(
                onSuccess = {
                    if (it.resultCode == "SUCCESS") {
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

    suspend fun addTestUser(): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val user = UserRealm().apply {
                this.name = "test_user"
            }
            userDao.setUser(user)

            Result.success(true)
        }
}