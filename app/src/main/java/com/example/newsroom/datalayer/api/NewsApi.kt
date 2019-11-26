package com.example.newsroom.datalayer.api

import com.example.newsroom.datalayer.model.News
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//Api call with headers

interface NewsApi {
    @GET("top-headlines")
    suspend fun getNews(@Query("country") country:String?,@Query("category") category:String?,@Query("apiKey")apiKey:String): Response<News>
}