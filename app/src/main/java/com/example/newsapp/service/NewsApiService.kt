package com.example.newsapp.service

import com.example.newsapp.model.News
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NewsApiService {
    //https://newsapi.org/v2/
    private val NEWS_BASE_URL = "https://newsapi.org/v2/"

    private val news_api = Retrofit.Builder()
        .baseUrl(NEWS_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NewsAPI::class.java)

    fun getNews(query : String, date :String, sortBy : String) : Single<News>{
        return news_api.getNews(query,date,sortBy)
    }

}