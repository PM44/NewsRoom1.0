package com.example.newsroom.view.home

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import com.example.newsroom.datalayer.model.Articles
import com.example.newsroom.newsItem

//class to show recycler view for news article using epoxy library
class NewsController(private val onClicktest: (view: View, article: Articles) -> Unit) :
    TypedEpoxyController<List<Articles>>() {
    
    private var id: Int = 1001
    
    override fun buildModels(data: List<Articles>?) {
        data?.forEach { article ->
            newsItem {
                id("${++id}")
                imageUrl(article.urlToImage)
                title(article.title)
                onClick { view ->
                    onClicktest(view, article)
                }
            }
        }
    }
}


