package com.lee.oneweekonebook.ui.wish.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.Book
import com.lee.oneweekonebook.utils.ioDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WishBookAddViewModel(private val bookDao: BookDatabaseDao, application: Application) : AndroidViewModel(application) {

    private var book = MutableLiveData<Book?>()

    fun saveBook(book: Book) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                bookDao.insert(book)
            }
        }
    }

}

class WishBookAddViewModelFactory(
    private val bookDatabaseDao: BookDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishBookAddViewModel::class.java)) {
            return WishBookAddViewModel(bookDatabaseDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}