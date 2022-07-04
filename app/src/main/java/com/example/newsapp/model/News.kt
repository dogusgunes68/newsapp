package com.example.newsapp.model

data class News(val status:String?,val totalResults:Long?,val articles:List<Article>) {
}