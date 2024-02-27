package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.android.utils.NetworkUtils
import com.lee.remember.android.utils.getErrorMessage
import com.lee.remember.repository.AuthRepository
import com.lee.remember.repository.FriendRepository
import com.lee.remember.repository.MemoryRepository
import com.lee.remember.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class IntroUiState(
    val loading: Boolean = false,
    val testUserResult: Boolean = false,
)

data class SplashUiState(
    val isFirst: Boolean? = null,
    val isLocalMode: Boolean? = null,
    val isAuthSuccess: Boolean? = null,
)

data class SignUpUiState(
    val loading: Boolean = false,
    val signupResult: Boolean = false,
    val emailCodeResult: String = "",
    val message: String = "",
)

//sealed interface SignUpUiState {
//    object Loading : SignUpUiState
//    data class Error(val message: String) : SignUpUiState
//    data class SignupResult(val result: Result<SignupResponse>) : SignUpUiState
//    data class EmailCodeResult(val result: Result<EmailResponse>) : SignUpUiState
//}

class IntroViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val friendRepository: FriendRepository,
    private val memoryRepository: MemoryRepository,
    private val networkUtils: NetworkUtils,
) : ViewModel() {

    private val _introUiState = MutableStateFlow(IntroUiState())
    val introUiState: StateFlow<IntroUiState> = _introUiState.asStateFlow()

    fun addTestUser() {
        viewModelScope.launch {
            _introUiState.update { it.copy(loading = true) }
            val result = userRepository.addTestUser()
            _introUiState.update { it.copy(loading = false, testUserResult = result.isSuccess) }
        }
    }


    private val _splashUiState = MutableStateFlow(SplashUiState())
    val splashUiState: StateFlow<SplashUiState> = _splashUiState.asStateFlow()

    fun initUserState() {
        if (!networkUtils.isNetworkAvailable()) {
            _splashUiState.update {
                it.copy(isLocalMode = true)
            }
            return
        }

        val user = userRepository.getUser()
        if (user != null && user.isLocalMode) {
            _splashUiState.update {
                it.copy(isLocalMode = true)
            }
            return
        }

        val token = authRepository.getToken()
        if (token == null) {
            _splashUiState.update {
                it.copy(isFirst = true)
            }
        } else {
//            viewModelScope.launch {
//                val result = userRepository.fetchUser()
//                _uiState.update {
//                    it.copy(isAuthSuccess = result.isSuccess)
//                }
//            }
            fetchUser()
        }
    }

    private fun fetchUser() {
        viewModelScope.launch {
            val result = userRepository.fetchUser()

            result.fold(
                onSuccess = {
                    friendRepository.fetchFriends()
                    memoryRepository.fetchMemories()

                    _splashUiState.update {
                        it.copy(isAuthSuccess = true)
                    }
                },
                onFailure = {
                    _splashUiState.update {
                        it.copy(isAuthSuccess = false)
                    }
                }
            )
        }
    }


    private val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState: StateFlow<SignUpUiState> = _signUpUiState.asStateFlow()

    fun sendEmailCode(email: String) {
        viewModelScope.launch {
            _signUpUiState.update { state ->
                state.copy(loading = true)
            }

            val result = authRepository.sendEmailCode(email)
            result.fold(
                onSuccess = {
                    _signUpUiState.update { state ->
                        state.copy(loading = false, emailCodeResult = it.result?.code ?: "")
                    }
                },
                onFailure = {
                    val errorMessage = getErrorMessage(it.message)
                    _signUpUiState.update { state ->
                        state.copy(loading = false, message = errorMessage)
                    }
                }
            )
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signUp(email, password)
            result.fold(
                onSuccess = {
                    _signUpUiState.update { state ->
                        state.copy(loading = false, signupResult = true)
                    }
                },
                onFailure = {
                    val errorMessage = getErrorMessage(it.message)
                    _signUpUiState.update { state ->
                        state.copy(loading = false, message = errorMessage)
                    }
                }
            )
        }
    }

    fun updateUserName(nickname: String) {
        viewModelScope.launch {
            val result = userRepository.updateUserName(nickname)
            result.fold(
                onSuccess = {
                    _signUpUiState.update { state ->
                        state.copy(loading = false, signupResult = true)
                    }
                },
                onFailure = {
                    _signUpUiState.update { state ->
                        state.copy(loading = false, message = it.message ?: "")
                    }
                }
            )
        }
    }

    fun resetSignUpUiState() {
        _signUpUiState.value = SignUpUiState()
    }

    fun resetSplashUiState() {
        _splashUiState.value = SplashUiState()
    }
}