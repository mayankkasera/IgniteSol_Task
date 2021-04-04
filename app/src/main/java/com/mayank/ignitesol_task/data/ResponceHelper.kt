package com.mayank.ignitesol_task.data

import com.mayank.ignitesol_task.util.ErrorUtils
import com.nehak.gutenberg_task.models.Result
import retrofit2.Response
import retrofit2.Retrofit

object ResponceHelper {
    suspend fun <T> getResponse(request: suspend () -> Response<T>,retrofit : Retrofit,defaultErrorMessage: String): Result<T> {
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