package com.example.newsapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.model.Article
import com.example.newsapp.model.News
import com.example.newsapp.room.NewsDatabase
import com.example.newsapp.service.NewsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : BaseViewModel(application) {

    val news = MutableLiveData<News>()
    val newsError = MutableLiveData<Boolean>()
    val newsLoading = MutableLiveData<Boolean>()

    private val disposable = CompositeDisposable()
    private val newsApiService : NewsApiService = NewsApiService()
    private val newsDatabase = NewsDatabase(application)
    private val newsDao = newsDatabase.newsDao()

    fun getNewsFromInternet(content : String,date:String,sortBy:String){
        newsLoading.value = true
        disposable.add(newsApiService.getNews(content,date,sortBy)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<News>(){
                override fun onSuccess(t: News) {

                    news.value = t
                    newsError.value = false
                    newsLoading.value = false
                }

                override fun onError(e: Throwable) {
                    newsLoading.value = false
                    newsError.value = true
                    println(e.localizedMessage)
                }

            })
        )
    }

    val article = MutableLiveData<Article>()
    val articleList = MutableLiveData<List<Article>>()
    val articleError = MutableLiveData<Boolean>()
    val articleLoading = MutableLiveData<Boolean>()

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