package com.mega.app.datalayer.model.response

data class ApiError(val code: String,
                    val msg: String,
                    var httpCode: Int,
                    val meta: Map<String, String>? = null) : Throwable()