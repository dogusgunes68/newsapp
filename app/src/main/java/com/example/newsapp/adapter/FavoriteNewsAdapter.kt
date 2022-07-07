package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.util.downloadImage
import com.example.newsapp.util.makePlaceHolder

class FavoriteNewsAdapter(val articles : ArrayList<Article>) : RecyclerView.Adapter<FavoriteNewsAdapter.FavoritesHolder>(){
    class FavoritesHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var favoriteButton = itemView.findViewById<ImageButton>(R.id.addFavoriteButton)
        var titleTextView = itemView.findViewById<TextView>(R.id.recyclerNewsTitleText)
        var newsImageView = itemView.findViewById<ImageView>(R.id.recyclerNewsImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesHolder {
        return FavoritesHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_row,parent,false))
    }

    override fun onBindViewHolder(holder: FavoritesHolder, position: Int) {
        holder.titleTextView.text = articles[position].title
        articles[position].urlToImage?.let { holder.newsImageView.downloadImage(it, makePlaceHolder(holder.itemView.context)) }

        if (articles[position].isItFavorite){
            holder.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_black)
        }


    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setArticles(newArticles : ArrayList<Article>){
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }

}