package com.mayank.ignitesol_task.data.datasource

import com.mayank.ignitesol_task.data.request.BookRequest
import com.mayank.ignitesol_task.util.ErrorUtils
import com.nehak.gutenberg_task.models.BookResponse
import com.nehak.gutenberg_task.models.Result
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class BooksRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {
    suspend fun fetchListOfBooks(): Result<BookResponse> {
        val bookService = retrofit.create(BookRequest::class.java);
        return getResponse(
            request = { bookService.getListOfBooks() },
            defaultErrorMessage = "Error fetching Books list")

    }

    private suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error", null)
        }
    }
}