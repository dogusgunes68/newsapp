package com.example.newsapp.adapter

import android.view.View
import com.example.newsapp.model.Article

interface ClickListener {
    fun addFavorites(article: Article)
    fun newsClicked(view : View)
}