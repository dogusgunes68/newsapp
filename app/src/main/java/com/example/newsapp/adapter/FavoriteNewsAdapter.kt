package com.example.newsapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.room.NewsDao
import com.example.newsapp.util.downloadImage
import com.example.newsapp.util.makePlaceHolder
import com.example.newsapp.viewmodel.NewsDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

class FavoriteNewsAdapter(val articles : ArrayList<Article>,val newsDetailViewModel: NewsDetailViewModel) : RecyclerView.Adapter<FavoriteNewsAdapter.FavoritesHolder>(){
    class FavoritesHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var layout = itemView.findViewById<ConstraintLayout>(R.id.constraint_layout)
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

        holder.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_black)


        holder.favoriteButton.setOnClickListener {
            runBlocking {
                launch {
                    newsDetailViewModel.deleteArticleFromRoom(articles[position].uuid)
                    notifyItemRemoved(position)
                }

            }

        }

        holder.layout.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong("position", position.toLong())
            Navigation.findNavController(it).navigate(R.id.action_favoritesFragment_to_newsDetailFragment,bundle)
        }

    }

    override fun getItemCount(): Int {
        if (articles.isNullOrEmpty()){
            return 0
        }
        return articles.size
    }

    fun setArticles(newArticles : ArrayList<Article>){
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }


}