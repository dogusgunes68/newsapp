package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.model.Article
import com.example.newsapp.util.downloadImage
import com.example.newsapp.util.makePlaceHolder

class NewsRecyclerAdapter(var articles: ArrayList<Article>) : RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>(),Filterable {

    var articleAll = articles
    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val newsTitleText = view.findViewById<TextView>(R.id.recyclerNewsTitleText)
        val newsImageView = view.findViewById<ImageView>(R.id.recyclerNewsImageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return  NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_row,parent,false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        holder.newsTitleText.text = articles.get(position).title
        articles.get(position).urlToImage?.let { holder.newsImageView.downloadImage(it, makePlaceHolder(holder.itemView.context)) }

    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setNewsArticle(articles: List<Article>){
        this.articles.clear()
        this.articles.addAll(articles)
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
                            if (article.title.toLowerCase().contains(constraint.toString().toString())){
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
                articles.addAll(results.values as Collection<Article>)
            }
            notifyDataSetChanged()
        }

    }

    override fun getFilter(): Filter {
        return filterr
    }

}