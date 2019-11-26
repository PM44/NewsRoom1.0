package com.example.newsroom.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.newsroom.datalayer.model.Articles
import com.example.newsroom.datalayer.db.NewsDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {
    val articleList = MutableLiveData<Articles>()
//to fetch the data from database using unique id
    fun fetch(uuid: Int) {
        launch {
            val article = NewsDatabase(getApplication()).newsDao().getArticle(uuid)
            articleList.value = article
        }
    }
}