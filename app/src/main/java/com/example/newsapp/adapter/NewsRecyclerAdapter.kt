package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.util.downloadImage
import com.example.newsapp.util.makePlaceHolder
import com.example.newsapp.viewmodel.NewsViewModel

class NewsRecyclerAdapter(var articles: ArrayList<Article>,val newsViewModel: NewsViewModel) : RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>(),Filterable {

    var articleAll : ArrayList<Article> = ArrayList(articles)

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view){



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
                newsViewModel.addDataToRoom(articles[position])
            }else{
                println("favorite")
            }

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