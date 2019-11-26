package com.mega.app.datalayer.mapi

import android.util.Log
import com.google.gson.Gson
import com.mega.app.async.AsyncResult
import com.mega.app.datalayer.model.response.ApiError
import kotlinx.coroutines.runBlocking
import retrofit2.Response

typealias NetworkCall<T> = suspend () -> Response<T>

private const val TAG = "apiCall"

fun <T : Any> apiCall(block: NetworkCall<T>): AsyncResult<T> {
    var result = AsyncResult<T>()

    runBlocking {
        worker(block,
                false,
                { result = result.success(it) },
                { e, m -> result = result.error(e, m) }
        )
    }

    return result
}

private suspend fun <T> worker(block: NetworkCall<T>,
                       retry: Boolean = false,
                       onSuccess: (T) -> Unit,
                       onFailure: (ApiError, String) -> Unit) {

    try {
        val response = block()

        if (response.isSuccessful) {
            val body = response.body()

            body?.let {
                onSuccess(it)

            } ?: "error".let {
                if (response.code() == 200) {
                    Log.d(TAG, "empty body on 200 response")
                }
                onFailure(ApiError("internal", "Unexpected response from server", response.code()), "Unexpected response")
            }

        } else {
            Log.e(TAG, response.message())


            var errorMsg = "Something went wrong"

            try {
                val error = Gson().fromJson(response.errorBody()?.charStream(), ApiError::class.java)
                error.httpCode = response.code()
                Log.d(TAG, "api error $error")

                if (error.httpCode in 400..499) {
                    onFailure(error, error.msg)
                } else {
                    onFailure(error, errorMsg)
                }

            } catch (t: Throwable) {
                Log.w(TAG, "error in parsing error response to ApiError", t)
                errorMsg = response.message() ?: errorMsg
                onFailure(ApiError("internal", errorMsg, response.code()), errorMsg)
            }
        }

    } catch (e: Throwable) {
        Log.e(TAG, "error", e)
        onFailure(ApiError("", "", 0), "Cannot Reach Mega. Please check your connection and try again")
    }

}
