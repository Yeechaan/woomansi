package com.lee.oneweekonebook.ui.wish.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lee.oneweekonebook.database.BookDatabaseDao
import com.lee.oneweekonebook.database.model.BOOK_TYPE_READING
import com.lee.oneweekonebook.database.model.BOOK_TYPE_WISH
import com.lee.oneweekonebook.utils.ioDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WishBookViewModel(private val bookDao: BookDatabaseDao, application: Application) : AndroidViewModel(application) {

    val books = bookDao.getBooksByType(BOOK_TYPE_WISH)

    fun addReadingBook(bookId: Int) {
        viewModelScope.launch {
            val currentBook = bookDao.getBook(bookId)
            currentBook.apply {
                type = BOOK_TYPE_READING
            }

            withContext(ioDispatcher) {
                bookDao.update(currentBook)
            }
        }
    }

    fun deleteBook(bookId: Int) {
        viewModelScope.launch(ioDispatcher) {
            bookDao.deleteBook(bookId)
        }
    }

}

class WishBookViewModelFactory(
    private val bookDatabaseDao: BookDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishBookViewModel::class.java)) {
            return WishBookViewModel(bookDatabaseDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}