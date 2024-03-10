package com.lee.remember.android.viewmodel.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.android.utils.getErrorMessage
import com.lee.remember.repository.AuthRepository
import com.lee.remember.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignUpUiState(
    val loading: Boolean = false,
    val signupResult: Boolean = false,
    val emailCodeResult: String = "",
    val message: String = "",
)

class SignUpViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
): ViewModel() {

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
}