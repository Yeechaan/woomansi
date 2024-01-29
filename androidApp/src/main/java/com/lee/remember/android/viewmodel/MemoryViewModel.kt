package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.local.dao.FriendDao
import com.lee.remember.model.Memory
import com.lee.remember.model.asData
import com.lee.remember.remote.request.MemoryRequest
import com.lee.remember.repository.MemoryRepository
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
    val friendDao: FriendDao = FriendDao(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(MemoryUiState())
    val uiState: StateFlow<MemoryUiState> = _uiState.asStateFlow()


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
            val result = memoryRepository.addMemory(request)
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
            memoryRepository.deleteMemory(memoryId)
        }
    }

    fun resetUiState() {
        _uiState.value = MemoryUiState()
    }
}