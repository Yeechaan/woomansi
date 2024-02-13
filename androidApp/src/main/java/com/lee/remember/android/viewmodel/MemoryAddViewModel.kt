package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.android.Contract
import com.lee.remember.local.model.UserRealm
import com.lee.remember.remote.request.MemoryRequest
import com.lee.remember.repository.FriendRepository
import com.lee.remember.repository.MemoryRepository
import com.lee.remember.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//sealed interface MemoryAddUiState {
//    data class Initialize(val name: String = "", val contracts: List<Contract> = listOf()) : MemoryAddUiState
//    object Loading : MemoryAddUiState
//    object Success : MemoryAddUiState
//    data class Error(val message: String) : MemoryAddUiState
//}

data class MemoryAddUiState(
    val name: String = "",
    val contracts: MutableList<Contract> = mutableListOf(),
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val message: String = ""
    )

class MemoryAddViewModel(
    private val memoryRepository: MemoryRepository,
    private val friendRepository: FriendRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val user = userRepository.getUser() ?: UserRealm()

    private val _uiState = MutableStateFlow<MemoryAddUiState>(MemoryAddUiState())
    val uiState: StateFlow<MemoryAddUiState> = _uiState

    fun getFriendName(friendId: Int) {
        viewModelScope.launch {
            val name = friendRepository.getFriend(friendId)?.name ?: ""
            _uiState.update { it.copy(name = name) }
//            _uiState.value = (_uiState.value as MemoryAddUiState.Initialize).copy(name = name)
        }
    }

    fun getFriendList(friendId: Int) {
        val contracts = friendRepository.getFriends().filter { it.id != friendId }.map {
            Contract(id = it.id.toString(), name = it.name, number = it.phoneNumber, isChecked = false)
        }.toMutableList()

        _uiState.update { it.copy(contracts = contracts) }
//        _uiState.value = (_uiState.value as MemoryAddUiState.Initialize).copy(contracts = contracts)
    }

    fun addMemory(request: MemoryRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = if (user.isLocalMode) {
                memoryRepository.addMemoryToLocal(request)
            } else {
                memoryRepository.addMemory(request)
            }

            result.fold(
                onSuccess = {
//                    _uiState.value = MemoryAddUiState.Success
                    _uiState.update { it.copy(isSuccess = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(isSuccess = false, message = it.message ?: "") }
//                    _uiState.value = MemoryAddUiState.Error(it.message ?: "")
                }
            )
        }
    }

    fun resetUiState() {
        _uiState.value = MemoryAddUiState()
    }

}