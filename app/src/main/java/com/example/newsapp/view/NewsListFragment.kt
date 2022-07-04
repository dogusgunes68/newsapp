package com.example.newsapp.view

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsRecyclerAdapter
import com.example.newsapp.databinding.FragmentNewsListBinding
import com.example.newsapp.model.Article
import com.example.newsapp.viewmodel.NewsViewModel

class NewsListFragment : Fragment() {

    private lateinit var newsViewModel : NewsViewModel
    private lateinit var binding : FragmentNewsListBinding
    private var newsRecyclerAdapter = NewsRecyclerAdapter(arrayListOf())
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

        newsViewModel.getNewsFromInternet("Apple", "2022-06-29", "popularity")
        observeNews()

        binding.topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
        }


        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    // Handle favorite icon press
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
        var searchView = mSearchItem.actionView as androidx.appcompat.widget.SearchView

        val theTextArea: SearchView.SearchAutoComplete =
            searchView.findViewById<View>(R.id.search_src_text) as SearchView.SearchAutoComplete

        theTextArea.setTextColor(Color.WHITE)

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
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
                newsRecyclerAdapter = NewsRecyclerAdapter(it.articles as ArrayList<Article>)
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