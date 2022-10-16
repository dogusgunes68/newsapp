package com.example.newsapp.service

import com.example.newsapp.model.News
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {


    @GET("YOUR_API_KEY")
    fun getNews(@Query("q") query : String, @Query("from") date : String, @Query("sortBy") sortBy : String):Single<News>
    


}