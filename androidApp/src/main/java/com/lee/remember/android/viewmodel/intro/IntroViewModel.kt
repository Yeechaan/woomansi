package com.lee.remember.android.viewmodel.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class IntroViewModel(
    private val userRepository: UserRepository,
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

}