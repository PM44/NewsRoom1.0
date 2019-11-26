package com.mega.app.async

data class AsyncResult<T>(
        val resultState: ResultState = ResultState.IN_PROGRESS,
        val errorMessage: String? = null,
        val error: Throwable? = null,
        val result: T? = null
) {

    fun isInProgress() = resultState == ResultState.IN_PROGRESS
    fun isSuccess() = resultState == ResultState.SUCCESS
    fun isError() = resultState == ResultState.ERROR

    fun success(result: T): AsyncResult<T> {
        return copy(resultState = ResultState.SUCCESS, result = result)
    }

    fun error(message: String?): AsyncResult<T> {
        return copy(resultState = ResultState.ERROR, errorMessage = message)
    }

    fun error(t: Throwable?, message: String?): AsyncResult<T> {
        return copy(resultState = ResultState.ERROR, error = t, errorMessage = message)
    }

    companion object {
        fun <T> success(result: T): AsyncResult<T> {
            return AsyncResult(ResultState.SUCCESS, result = result)
        }

        fun <T> error(t: Throwable?, message: String?): AsyncResult<T> {
            return AsyncResult(resultState = ResultState.ERROR, error = t, errorMessage = message)
        }
    }
}