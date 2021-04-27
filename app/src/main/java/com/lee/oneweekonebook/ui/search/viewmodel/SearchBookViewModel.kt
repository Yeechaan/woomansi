package com.lee.oneweekonebook.ui.search.viewmodel

import androidx.lifecycle.*
import com.lee.oneweekonebook.network.BookApi
import com.lee.oneweekonebook.ui.search.model.BookInfo
import com.lee.oneweekonebook.ui.search.model.asBookList
import com.lee.oneweekonebook.utils.ioDispatcher
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchBookViewModel : ViewModel() {

    private val _books = MutableLiveData<List<BookInfo>>()
    val books: LiveData<List<BookInfo>>
        get() = _books

    fun searchBook(query: String) {
        viewModelScope.launch {
            try {
                val response = withContext(ioDispatcher) {
                    BookApi.bookApiService.searchBookAsync(query = query).await()
                }
                _books.value = response.asBookList()
                Logger.d(response)
            } catch (e: Exception) {
                Logger.d(e.message)
            }
        }
    }

}

class SearchBookViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchBookViewModel::class.java)) {
            return SearchBookViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}