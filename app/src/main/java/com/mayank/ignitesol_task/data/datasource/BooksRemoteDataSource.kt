package com.mayank.ignitesol_task.data.datasource

import com.mayank.ignitesol_task.data.ResponceHelper
import com.mayank.ignitesol_task.data.request.BookRequest
import com.mayank.ignitesol_task.util.ErrorUtils
import com.nehak.gutenberg_task.models.BookResponse
import com.nehak.gutenberg_task.models.Result
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class BooksRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {
    suspend fun fetchListOfBooks(topic: String, page: Int): Result<BookResponse> {
        val bookService = retrofit.create(BookRequest::class.java);
        return ResponceHelper.getResponse(
            request = { bookService.getListOfBooks(topic, page) },
            retrofit = retrofit,
            defaultErrorMessage = "Error fetching Books list")
    }


}