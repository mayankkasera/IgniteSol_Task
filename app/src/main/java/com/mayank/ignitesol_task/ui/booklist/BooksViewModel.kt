package com.mayank.ignitesol_task.ui.booklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayank.ignitesol_task.data.repository.BooksRepository
import com.nehak.gutenberg_task.models.BookResponse
import com.nehak.gutenberg_task.models.Result
import kotlinx.coroutines.flow.collect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(private val booksRepository: BooksRepository) : ViewModel()  {

    private val _bookList = MutableLiveData<Result<BookResponse?>>()
    val booksList = _bookList



    fun fetchBooks(topic: String, page: Int) {
        viewModelScope.launch {
            booksRepository.fetchBooks(topic, page).collect {
                _bookList.value = it
            }
        }
    }
}