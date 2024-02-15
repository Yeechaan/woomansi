package com.lee.remember.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.remember.local.model.UserRealm
import com.lee.remember.model.Memory
import com.lee.remember.model.asData
import com.lee.remember.repository.FriendRepository
import com.lee.remember.repository.MemoryRepository
import com.lee.remember.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MemoryFriendUiState(
    val name: String = "",
    val memories: List<Memory> = listOf(),
    val loading: Boolean = false,
    val success: Boolean = false,
    val message: String = "",
)

class MemoryFriendViewModel(
    private val friendId: Int?,
    private val memoryRepository: MemoryRepository,
    private val friendRepository: FriendRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MemoryFriendUiState())
    val uiState: StateFlow<MemoryFriendUiState> = _uiState

    private val user = userRepository.getUser() ?: UserRealm()

    init {
        viewModelScope.launch {
            val name = friendRepository.getFriend(friendId ?: -1)?.name ?: ""
            _uiState.update { it.copy(name = name) }

            memoryRepository.getMemoriesByFriendIdAsFlow(friendId ?: -1).collectLatest {
                val memories = it.map { it.asData() }
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

}