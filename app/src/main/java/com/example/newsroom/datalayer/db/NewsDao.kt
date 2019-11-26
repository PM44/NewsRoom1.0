package com.example.newsroom.datalayer.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.newsroom.datalayer.model.Articles

//Query to store or update database
@Dao
interface NewsDao {
    @Insert
    suspend fun insertAll(vararg articles: Articles):List<Long>

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles():List<Articles>

    @Query("SELECT * FROM articles WHERE uuid=:newsId")
    suspend fun getArticle(newsId:Int): Articles

    @Query("DELETE FROM articles")
    suspend fun deleteAll()
}