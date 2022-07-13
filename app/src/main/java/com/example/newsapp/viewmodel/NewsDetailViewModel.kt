package com.example.newsapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.adapter.FavoriteNewsAdapter
import com.example.newsapp.model.Article
import com.example.newsapp.room.NewsDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    fun getAllArticleFromRoom(adapter: FavoriteNewsAdapter,position: Int){

        articleLoading.value = true

        launch {

            var articles = newsDao.getAllArticles()
            articleList.value = articles
            println("get all article")
            articleLoading.value = false
        }

    }

    fun getAllArticleFromRoom() : List<Article>{

        articleLoading.value = true

        val list = arrayListOf<Article>()
        runBlocking {
            var articles = newsDao.getAllArticles()
            articleList.value = articles
            articleLoading.value = false
            list.addAll(articles)
        }

        return list

    }

    fun deleteArticleFromRoom(articleId : Long){
        runBlocking {
            newsDao.deleteArticle(articleId)
            println("delete")
            getAllArticleFromRoom()
        }

    }


}