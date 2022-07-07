package com.example.newsapp.service

import com.example.newsapp.model.News
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    //https://newsapi.org/v2/everything?q=tesla&from=2022-05-30&sortBy=publishedAt&apiKey=cf99fa1cacab44c6a7ef0fd06955d1a5
    //cf99fa1cacab44c6a7ef0fd06955d1a5
    //https://newsapi.org/v2/everything?q=Apple&from=2022-06-29&sortBy=popularity&apiKey=cf99fa1cacab44c6a7ef0fd06955d1a5
    @GET("everything???&apiKey=cf99fa1cacab44c6a7ef0fd06955d1a5")
    fun getNews(@Query("q") query : String, @Query("from") date : String, @Query("sortBy") sortBy : String):Single<News>
    


}