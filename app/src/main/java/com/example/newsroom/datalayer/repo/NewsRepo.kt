package com.example.newsroom.datalayer.repo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import com.example.newsroom.datalayer.api.NewsApiService
import com.example.newsroom.datalayer.db.NewsDatabase
import com.example.newsroom.datalayer.model.Articles
import com.example.newsroom.datalayer.model.News
import com.example.newsroom.util.SharedPrefrenceHelper
import com.mega.app.async.AsyncResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import kotlin.coroutines.CoroutineContext

class NewsRepo : CoroutineScope {

    private val job = Job()
    private var REFRESH_TIME = 5 * 60 * 1000 * 1000 * 1000L
    private lateinit var prefrenceHelper: SharedPrefrenceHelper
    private lateinit var context: Context


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main


    private fun verifyAvailableNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    suspend fun fetchData(context: Context, selectedCountry: String?, selectedCategory: String?): AsyncResult<News>? {
        prefrenceHelper = SharedPrefrenceHelper(context)
        this.context = context
        checkCacheDuration()
        val update_time = prefrenceHelper.getUpdateTime()
        if (verifyAvailableNetwork(context)) {
            if (update_time != null && update_time != 0L && System.nanoTime() - update_time < REFRESH_TIME) {
                val result = NewsApiService().getNews(selectedCountry, selectedCategory)
                if (result.isSuccess()) {
                    saveDataLocally(result.result)
                    return result
                } else {
                    return fetchDataLocally()
                }
            } else {
                return fetchDataLocally()
            }
        } else {
            return fetchDataLocally()
        }
    }

    private suspend fun fetchDataLocally(): AsyncResult<News> {
        val articles = NewsDatabase(context).newsDao().getAllArticles()
        val news = News("", 0, articles)
        return AsyncResult<News>().success(news)
    }

    private fun checkCacheDuration() {
        val cacheDuration = prefrenceHelper.getCacheDuration()
        try {
            val cacheDurationInt = cacheDuration?.toInt() ?: 5 * 60
            REFRESH_TIME = cacheDurationInt.times(1000 * 1000 * 1000L)

        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    //save data in database
    suspend fun saveDataLocally(news: News?) {
        launch {
            val dao = NewsDatabase(context).newsDao()
            dao.deleteAll()
            val result = news?.articles?.toTypedArray()?.let { dao.insertAll(*it) }
            var i = 0
            while (i < news!!.articles!!.size) {
                news.articles!!.get(i).uuid = result!!.get(i).toInt()
                i++
            }
        }
    }

}