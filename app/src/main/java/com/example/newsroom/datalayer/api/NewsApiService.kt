package com.example.newsroom.datalayer.api

import com.example.newsroom.datalayer.model.News
import com.mega.app.async.AsyncResult
import com.mega.app.datalayer.mapi.apiCall
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
//Retrofit class to make network call
class NewsApiService {
    private val BASE_URL = "https://newsapi.org/v2/"

    val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val builder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(builder.build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(NewsApi::class.java)



    suspend fun getNews(selectedCountry: String?, selectedCategory: String?): AsyncResult<News> {
        return apiCall {
            api.getNews(selectedCountry,selectedCategory,"6d3634affd6e46d3ac8aecf861a4d7a2")
        }
    }
}


