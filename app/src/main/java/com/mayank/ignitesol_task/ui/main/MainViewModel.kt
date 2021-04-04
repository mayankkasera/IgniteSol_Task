package com.mayank.ignitesol_task.ui.main

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
class MainViewModel @Inject constructor(private val booksRepository: BooksRepository) : ViewModel()  {

    private val _bookList = MutableLiveData<Result<BookResponse?>>()
    val booksList = _bookList

    init {
        fetchBooks()
    }

    private fun fetchBooks() {
        viewModelScope.launch {
            booksRepository.fetchBooks().collect {
                _bookList.value = it
            }
        }
    }
}