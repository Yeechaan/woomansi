package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.local.model.UserRealm
import com.lee.remember.model.Memory
import com.lee.remember.model.asData
import com.lee.remember.remote.request.MemoryRequest
import com.lee.remember.remote.request.MemoryUpdateRequest
import com.lee.remember.repository.FriendRepository
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
    private val memoryRepository: MemoryRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MemoryUiState())
    val uiState: StateFlow<MemoryUiState> = _uiState.asStateFlow()

    private val user = userRepository.getUser() ?: UserRealm()

    init {
        viewModelScope.launch {
            memoryRepository.getMemories().collectLatest { results ->
                val memories = results.list.map { it.asData() }.sortedByDescending { it.date }
                _uiState.update { it.copy(memories = memories) }
            }
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