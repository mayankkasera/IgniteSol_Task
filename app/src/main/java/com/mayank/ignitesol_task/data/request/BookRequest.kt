package com.mayank.ignitesol_task.data.request

import com.nehak.gutenberg_task.models.BookResponse
import retrofit2.Response
import retrofit2.http.GET

interface BookRequest {

    @GET("/books")
    suspend fun getListOfBooks() : Response<BookResponse>

}