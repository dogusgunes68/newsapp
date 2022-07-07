package com.example.newsapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.model.Article
import com.example.newsapp.room.NewsDatabase
import kotlinx.coroutines.launch

class NewsDetailViewModel(application: Application) : BaseViewModel(application) {

    val article = MutableLiveData<Article>()
    val articleList = MutableLiveData<List<Article>>()
    val articleError = MutableLiveData<Boolean>()
    val articleLoading = MutableLiveData<Boolean>()

    private val newsDatabase = NewsDatabase(application)
    private val newsDao = newsDatabase.newsDao()


    fun addDataToRoom(a: Article){

        launch {
            var long = newsDao.insertArticle(a)
            a.uuid = long

            article.value = a

        }

    }

    fun getAllArticleFromRoom(){

        articleLoading.value = true

        launch {
            var articles = newsDao.getAllArticles()
            articleList.value = articles
            articleLoading.value = false
        }


    }


}