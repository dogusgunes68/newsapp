package com.example.newsapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsDetailPageBinding
import com.example.newsapp.model.Article
import com.example.newsapp.model.Source
import com.example.newsapp.util.downloadImage
import com.example.newsapp.util.makePlaceHolder
import com.example.newsapp.viewmodel.NewsDetailViewModel
import com.example.newsapp.viewmodel.NewsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NewsDetailFragment : Fragment() {

    private lateinit var binding : FragmentNewsDetailPageBinding
    private lateinit var newsViewModel: NewsViewModel
    private var article = Article(Source("null","null"),"null","null","null","null","null","null"
    ,"null")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_detail_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewsDetailPageBinding.bind(view)

        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
        val date = LocalDateTime.now().toString()
        newsViewModel.getNewsFromInternet("Apple",date.substring(0,10),"popularity")
        var articlePosition : Long
        arguments.let {
            articlePosition = arguments?.getLong("position")!!.toLong()
        }


        observeArticle(articlePosition)



    }



    fun observeArticle(position : Long){
        newsViewModel.news.observe(viewLifecycleOwner, Observer { news ->
            news.let {
                if (news != null){
                    article = news.articles[position.toInt()]
                    binding.articleImage.downloadImage(article.urlToImage!!, makePlaceHolder(requireContext()))
                    binding.articleTitleText.text = article.title
                    binding.articleSourceText.text = article.source!!.name
                    binding.articleAuthorText.text = article.author
                    binding.articleContentText.text = article.content
                    binding.articlePublishedAtText.text = "Published at: "+article.publishedAt

                    println(article.content)

                }
            }

        })
    }


}