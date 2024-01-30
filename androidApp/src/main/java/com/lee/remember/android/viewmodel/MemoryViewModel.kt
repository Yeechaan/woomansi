package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.local.dao.FriendDao
import com.lee.remember.local.model.UserRealm
import com.lee.remember.model.Memory
import com.lee.remember.model.asData
import com.lee.remember.remote.request.MemoryRequest
import com.lee.remember.remote.request.MemoryUpdateRequest
import com.lee.remember.repository.MemoryRepository
import com.lee.remember.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MemoryUiState(
    val name: String = "",
    val memories: List<Memory> = listOf(),
    val loading: Boolean = false,
    val success: Boolean = false,
    val message: String = "",
    val memory: Memory? = null,
)

class MemoryViewModel(
    val memoryRepository: MemoryRepository = MemoryRepository(),
    val userRepository: UserRepository = UserRepository(),
    val friendDao: FriendDao = FriendDao(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(MemoryUiState())
    val uiState: StateFlow<MemoryUiState> = _uiState.asStateFlow()

    val user = userRepository.getUser() ?: UserRealm()

    fun getAllMemories() {
        viewModelScope.launch {
            memoryRepository.getMemories().collectLatest {
                val memories = it.list.map { it.asData() }
                    .sortedByDescending { it.date }

                _uiState.update {
                    it.copy(memories = memories)
                }
            }
        }
    }

    fun getFriendMemories(friendId: Int) {
        viewModelScope.launch {
            memoryRepository.getMemories().collectLatest {
                val memories = it.list.map { it.asData() }
                    .sortedByDescending { it.date }
                    .filter { it.ownerFriend?.id == friendId }

                _uiState.update {
                    it.copy(memories = memories)
                }
            }
        }
    }

    fun getMemory(memoryId: Int) {
        viewModelScope.launch {
            val memory = memoryRepository.getMemory(memoryId)
            _uiState.update {
                it.copy(memory = memory?.asData())
            }
        }
    }

    fun getFriendName(friendId: Int) {
        viewModelScope.launch {
            val name = friendDao.getFriend(friendId)?.name ?: ""
            _uiState.update { it.copy(name = name) }
        }
    }

    fun addMemory(request: MemoryRequest) {
        viewModelScope.launch {
            val result = if (user.isLocalMode) {
                memoryRepository.addMemoryToLocal(request)
            } else {
                memoryRepository.addMemory(request)
            }

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(loading = false, success = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(loading = false, message = it.message) }
                }
            )
        }
    }

    fun updateMemory(memoryId: Int, request: MemoryUpdateRequest) {
        viewModelScope.launch {
            val result = if (user.isLocalMode) {
                memoryRepository.updateMemoryToLocal(memoryId, request)
            } else {
                memoryRepository.updateMemory(memoryId, request)
            }

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(loading = false, success = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(loading = false, message = it.message) }
                }
            )
        }
    }

    fun deleteMemory(memoryId: Int) {
        viewModelScope.launch {
            if (user.isLocalMode) {
                memoryRepository.deleteMemoryToLocal(memoryId)
            } else {
                memoryRepository.deleteMemory(memoryId)
            }
        }
    }

    fun resetUiState() {
        _uiState.value = MemoryUiState()
    }
}