package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

data class LoginUiState(
    val isFirst: Boolean? = null,
    val isLocalMode: Boolean? = null,
    val isAuthSuccess: Boolean? = null,
)

class IntroViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(IntroUiState())
    val uiState: StateFlow<IntroUiState> = _uiState.asStateFlow()

    fun initUserState() {
        val user = userRepository.getUser()
        if (user != null && user.isLocalMode) {
            _uiState.update {
                it.copy(isLocalMode = true)
            }
            return
        }

        val token = authRepository.getToken()
        if (token == null) {
            _uiState.update {
                it.copy(isFirst = true)
            }
        } else {
            viewModelScope.launch {
                val result = userRepository.fetchUser()
                _uiState.update {
                    it.copy(isAuthSuccess = result.isSuccess)
                }
            }
        }
    }

    fun fetchUser() {
        viewModelScope.launch {
            FriendRepository().fetchFriends()
            MemoryRepository().fetchMemories()
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
}