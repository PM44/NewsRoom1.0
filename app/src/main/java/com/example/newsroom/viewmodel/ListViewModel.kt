package com.example.newsroom.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.newsroom.util.SharedPrefrenceHelper
import com.example.newsroom.datalayer.model.Articles
import com.example.newsroom.datalayer.api.NewsApiService
import com.example.newsroom.datalayer.db.NewsDatabase
import com.example.newsroom.datalayer.repo.NewsRepo
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class ListViewModel(application: Application) : BaseViewModel(application) {
    private val disposable = CompositeDisposable()
    val articles = MutableLiveData<List<Articles>>()
    val newsError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private var prefHelper = SharedPrefrenceHelper(getApplication())




    fun getData() {
        launch {
            val result = NewsRepo().fetchData(getApplication(),prefHelper.getSelectedCountry(),prefHelper.getSelectedCategory())
            if (result!!.isSuccess()) {
                loading.value=false
                val value = result.result?.articles
                //NewsRepo().saveDataLocally(result.result)
                articles.value = value

            } else if(result.isInProgress()) {
                loading.value=true
            } else {
                loading.value=false
                newsError.value=false

            }
        }
    }


    // to know from where to take data according to cache value


    // to fetch data from database
    private fun fetchFromDatbase() {
        loading.value = true
        launch {
            val articles = NewsDatabase(getApplication()).newsDao().getAllArticles()
           // articlesRetrieved(articles)
            //Toast.makeText(getApplication(), "Articles retrieved from database", Toast.LENGTH_LONG).show()
        }
    }

    //to set the valuesof article
    /*private fun articlesRetrieved(articlesList: List<Articles>) {
        articles.value = articlesList
        newsError.value = false
        loading.value = false
    }*/


    //to fetch data from remote



    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun refreshBypassCache() {
        getData()
    }
}