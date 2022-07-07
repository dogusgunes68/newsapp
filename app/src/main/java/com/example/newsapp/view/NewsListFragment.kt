package com.example.newsapp.view

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsRecyclerAdapter
import com.example.newsapp.databinding.FragmentNewsListBinding
import com.example.newsapp.model.Article
import com.example.newsapp.viewmodel.NewsViewModel
import java.time.LocalDateTime

class NewsListFragment : Fragment() {

    private lateinit var newsViewModel : NewsViewModel
    private lateinit var binding : FragmentNewsListBinding
    private lateinit var newsRecyclerAdapter : NewsRecyclerAdapter
    private lateinit var mSearchItem : MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setHasOptionsMenu(true)
        binding = FragmentNewsListBinding.bind(view)
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)

        newsRecyclerAdapter = NewsRecyclerAdapter(arrayListOf(),newsViewModel)

        var date = LocalDateTime.now().toString()
        newsViewModel.getNewsFromInternet("Apple",date.substring(0,10) , "popularity")
        observeNews()

        binding.topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
        }


        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    Navigation.findNavController(view).navigate(NewsListFragmentDirections.actionNewsListFragment2ToFavoritesFragment())
                    true
                }
                R.id.search -> {
                    search()
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }



    }

    fun search(){
        var menu = binding.topAppBar.menu

        mSearchItem = menu.findItem(R.id.search)
        var searchView = mSearchItem.actionView as SearchView

        val theTextArea: SearchView.SearchAutoComplete =
            searchView.findViewById<View>(R.id.search_src_text) as SearchView.SearchAutoComplete

        theTextArea.setTextColor(Color.WHITE)

        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                newsRecyclerAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newsRecyclerAdapter.filter.filter(newText)
                return true
            }

        })

    }

    fun observeNews(){
        newsViewModel.newsLoading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                binding.newsRecyclerView.visibility = View.GONE
                binding.errorMessageText.visibility = View.GONE
                binding.loadingProgressBar.visibility = View.VISIBLE
            }
        })

        newsViewModel.news.observe(viewLifecycleOwner, Observer { news ->
            news.let {
                newsRecyclerAdapter.setNewsArticle(it.articles)
                binding.newsRecyclerView.adapter = newsRecyclerAdapter
                binding.newsRecyclerView.layoutManager = GridLayoutManager(context, 1)
                binding.newsRecyclerView.visibility = View.VISIBLE
                binding.errorMessageText.visibility = View.GONE
                binding.loadingProgressBar.visibility = View.GONE
            }
        })

        newsViewModel.newsError.observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                binding.newsRecyclerView.visibility = View.GONE
                binding.errorMessageText.visibility = View.VISIBLE
                binding.loadingProgressBar.visibility = View.GONE
            }
        })


    }


}