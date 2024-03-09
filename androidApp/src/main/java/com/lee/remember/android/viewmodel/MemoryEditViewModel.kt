package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.local.model.UserRealm
import com.lee.remember.model.Contract
import com.lee.remember.model.Memory
import com.lee.remember.model.asData
import com.lee.remember.remote.request.MemoryUpdateRequest
import com.lee.remember.repository.FriendRepository
import com.lee.remember.repository.MemoryRepository
import com.lee.remember.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MemoryEditUiState(
    val memory: Memory? = null,
    val contracts: MutableList<Contract> = mutableListOf(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String = "",
)

class MemoryEditViewModel(
    private val memoryId: Int?,
    private val memoryRepository: MemoryRepository,
    private val friendRepository: FriendRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MemoryEditUiState())
    val uiState: StateFlow<MemoryEditUiState> = _uiState

    private val user = userRepository.getUser() ?: UserRealm()

    init {
        viewModelScope.launch {
            val memory = memoryRepository.getMemory(memoryId ?: -1)?.asData()
            _uiState.update { it.copy(memory = memory) }

            val ownerFriendId = memory?.ownerFriend?.id
            val contracts = friendRepository.getFriends().filter { it.id != ownerFriendId }.map {
                Contract(id = it.id.toString(), name = it.name, number = it.phoneNumber, isChecked = false)
            }.toMutableList()
            _uiState.update { it.copy(contracts = contracts) }
        }
    }

    fun updateMemory(memoryId: Int, request: MemoryUpdateRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = if (user.isLocalMode) {
                memoryRepository.updateMemoryToLocal(memoryId, request)
            } else {
                memoryRepository.updateMemory(memoryId, request)
            }

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false, message = it.message) }
                }
            )
        }
    }

    fun resetUiState() {
        _uiState.value = MemoryEditUiState()
    }
}