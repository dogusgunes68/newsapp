package com.example.newsapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.newsapp.model.Article

@Dao
interface NewsDao {

    @Insert
    suspend fun insertArticle(article: Article):Long

    @Query("select * from article")
    suspend fun getAllArticles() : List<Article>

    @Query("select * from article where uuid = :articleId")
    suspend fun getArticle(articleId : Long) : Article

    @Query("delete from article where uuid = :articleId")
    suspend fun deleteArticle(articleId: Long)




}