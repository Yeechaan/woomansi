package com.lee.remember.repository

import com.lee.remember.local.dao.AuthDao
import com.lee.remember.local.dao.UserDao
import com.lee.remember.local.model.asRealm
import com.lee.remember.remote.UserApi
import com.lee.remember.remote.request.UserInfoRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class UserRepository(
    val userDao: UserDao = UserDao(),
    val userApi: UserApi = UserApi(),
    val authDao: AuthDao = AuthDao(),
) {

    fun getUser() = userDao.getUser()

    suspend fun updateUserName(name: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            val token = authDao.getToken()?.accessToken ?: return@withContext Result.failure(Exception(""))
            val user = getUser() ?: return@withContext Result.failure(Exception(""))
            val userInfoRequest = UserInfoRequest(user.email, user.password, name, user.phoneNumber, user.profileImage)
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
            val token = authDao.getToken()?.accessToken ?: return@withContext Result.failure(Exception(""))
            val result = userApi.getUser(token)

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
}