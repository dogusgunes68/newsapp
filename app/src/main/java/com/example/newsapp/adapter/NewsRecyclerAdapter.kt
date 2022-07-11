package com.example.newsapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.util.downloadImage
import com.example.newsapp.util.makePlaceHolder
import com.example.newsapp.viewmodel.NewsDetailViewModel
import com.example.newsapp.viewmodel.NewsViewModel
import kotlinx.coroutines.runBlocking

class NewsRecyclerAdapter(var articles: ArrayList<Article>,val newsViewModel: NewsViewModel,val newsDetailViewModel: NewsDetailViewModel) : RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>(),Filterable {

    var articleAll : ArrayList<Article> = ArrayList(articles)

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val layout = view.findViewById<ConstraintLayout>(R.id.constraint_layout)
        val newsTitleText = view.findViewById<TextView>(R.id.recyclerNewsTitleText)
        val newsImageView = view.findViewById<ImageView>(R.id.recyclerNewsImageView)
        val favoriteButton = view.findViewById<ImageButton>(R.id.addFavoriteButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return  NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_row,parent,false))
    }



    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        holder.newsTitleText.text = articles.get(position).title
        articles.get(position).urlToImage?.let { holder.newsImageView.downloadImage(it, makePlaceHolder(holder.itemView.context)) }



        if (articles[position].isItFavorite){
            holder.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_black)
        }



        holder.favoriteButton.setOnClickListener {
            if (!articles[position].isItFavorite){
                holder.favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_black)
                articles[position].isItFavorite = true

                newsDetailViewModel.addDataToRoom(articles[position])

            }else{
                println("favorite")
            }

        }

        holder.layout.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong("position", position.toLong())
            Navigation.findNavController(it).navigate(R.id.action_newsListFragment2_to_newsDetailFragment,bundle)
        }

    }

    override fun getItemCount(): Int {
        if (articles.size == 0){
            return 0
        }else{
            return articles.size
        }
    }

    fun setNewsArticle(articles: List<Article>){
        this.articles.clear()
        this.articles.addAll(articles)
        articleAll.clear()
        articleAll.addAll(articles)
        notifyDataSetChanged()
    }

    fun getNewsArticle() : List<Article>{
        return this.articles
    }

    var filterr = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filteredList = arrayListOf<Article>()
            constraint?.let {
                if (constraint.isEmpty()){
                    filteredList.addAll(articleAll)
                }else{
                    for (article in articleAll){
                        article.title?.let {
                            if (article.title.toLowerCase().contains(constraint.toString().toLowerCase())){
                                filteredList.add(article)
                            }
                        }
                    }
                }

            }
            val filterResults = FilterResults()
            filterResults.values = filteredList

            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            articles.clear()
            results?.let {
                articles.addAll(it.values as Collection<Article>)
                notifyDataSetChanged()
            }

        }

    }

    override fun getFilter(): Filter {
        return filterr
    }

}