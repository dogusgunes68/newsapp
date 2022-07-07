package com.example.newsapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapter.FavoriteNewsAdapter
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.example.newsapp.model.Article
import com.example.newsapp.viewmodel.NewsDetailViewModel
import com.example.newsapp.viewmodel.NewsViewModel

class FavoritesFragment : Fragment() {

    private lateinit var binding : FragmentFavoritesBinding
    private lateinit var newsViewModel : NewsDetailViewModel
    private var favoriteNewsAdapter = FavoriteNewsAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFavoritesBinding.bind(view)

        newsViewModel = ViewModelProviders.of(this).get(NewsDetailViewModel::class.java)

        newsViewModel.getAllArticleFromRoom()

        observeArticles()

        binding.backButton.setOnClickListener {
            Navigation.findNavController(it).navigate(FavoritesFragmentDirections.actionFavoritesFragmentToNewsListFragment2())
        }

    }

    fun observeArticles(){
        newsViewModel.articleList.observe(viewLifecycleOwner, Observer {
            if (!it.isEmpty()){
                favoriteNewsAdapter.setArticles(it as ArrayList<Article>)
                binding.favoritesRecyclerView.layoutManager = GridLayoutManager(context,1)
                binding.favoritesRecyclerView.adapter = favoriteNewsAdapter
                binding.favoritesRecyclerView.visibility = View.VISIBLE
            }
        })
    }

}