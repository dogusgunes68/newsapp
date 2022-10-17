package com.example.newsapp.view

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsRecyclerAdapter
import com.example.newsapp.databinding.FragmentNewsListBinding
import com.example.newsapp.viewmodel.NewsDetailViewModel
import com.example.newsapp.viewmodel.NewsViewModel
import java.time.LocalDateTime

class NewsListFragment : Fragment() {

    private lateinit var newsViewModel : NewsViewModel
    private lateinit var newsDetailViewModel: NewsDetailViewModel
    private lateinit var binding : FragmentNewsListBinding
    private lateinit var newsRecyclerAdapter : NewsRecyclerAdapter
    private lateinit var mSearchItem : MenuItem
    //lateinit var toggle : ActionBarDrawerToggle
    private lateinit var navController : NavController

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
        newsDetailViewModel = ViewModelProviders.of(this).get(NewsDetailViewModel::class.java)


        newsRecyclerAdapter = NewsRecyclerAdapter(arrayListOf(),newsViewModel,newsDetailViewModel)

        //It works on API LEVEL 26. that's why now became red
        var date = LocalDateTime.now().toString()

        newsViewModel.getNewsFromInternet("Apple",date.substring(0,10) , "popularity")
        observeNews()


        val drawerLayout : DrawerLayout = binding.drawerLayout
        drawerLayout.closeDrawers()
        binding.appBar.topAppBar.getChildAt(1).setOnClickListener {
           drawerLayout.openDrawer(GravityCompat.START)
        }




        binding.appBar.topAppBar.setOnMenuItemClickListener { menuItem ->
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
        var menu = binding.appBar.topAppBar.menu

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