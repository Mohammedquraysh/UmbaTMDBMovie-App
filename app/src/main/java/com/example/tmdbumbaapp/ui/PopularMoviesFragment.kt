package com.example.tmdbumbaapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbumbaapp.*
import com.example.tmdbumbaapp.adapter.PopularMoviesAdapter
import com.example.tmdbumbaapp.application.TMDBApplication
import com.example.tmdbumbaapp.databinding.FragmentPopularMoviesBinding
import com.example.tmdbumbaapp.model.LatestMovieResponseModel
import com.example.tmdbumbaapp.model.popularmoviemodel.PopularMoviesResponseModel
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.example.tmdbumbaapp.repository.Resource
import com.example.tmdbumbaapp.util.ApiCallNetworkResource
import com.example.tmdbumbaapp.util.ConnectivityLiveData
import com.example.tmdbumbaapp.util.MoviePreference
import com.example.tmdbumbaapp.util.observeNetworkConnection
import com.example.tmdbumbaapp.viewmodel.MoviesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularMoviesFragment : Fragment() {
    private lateinit var _binding: FragmentPopularMoviesBinding
    private val binding get() = _binding
    private lateinit var popularMoviesAdapter: PopularMoviesAdapter
    private lateinit var popularMoviesRecyclerView: RecyclerView
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var connectivityLiveData: ConnectivityLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityLiveData = ConnectivityLiveData(TMDBApplication.application)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       _binding = FragmentPopularMoviesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        popularMoviesRecyclerView = binding.popularMoviesRecyclerview
        initRecyclerView()
        updateListFromDatabase()


        /** observing network state */
        connectivityLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    //updateListFromDatabase()
                    viewModel.popularMovieLiveData.observe(viewLifecycleOwner, Observer {
                        when(it){
                            is Resource.Success -> {
                                hideProgressbar()
                                it.data?.let {
                                    popularMoviesAdapter.differ.submitList(it.results.toList())
                                    val totalPages = it.total_pages / QUERY_PAGE + 2
                                    isLastPage =viewModel.pageNumber == totalPages
                                    viewModel.savePopularMovieToDb(it.results.toList())
                                }
                            }
                            is Resource.Error -> {
                                hideProgressbar()
                                it.message?.let {
                                    Log.d("MQ", "An error occur: $it")
                                }
                            }
                            is Resource.Loading -> {
                                showProgressbar()
                            }
                        }
                    })
                }
              false -> {
                  updateListFromDatabase()
                  Snackbar.make(binding.root, "Network is not available", Snackbar.LENGTH_LONG).show()

              }

            }

        })


    }

    fun hideProgressbar(){
        binding.paginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    fun showProgressbar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }


    /** to handle pagination */
    var isLoading = false
    var isLastPage = false
    var isScrolling  = false
    val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val visibleFirstItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val  totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isLastItemCount = visibleFirstItemPosition + visibleItemCount >= totalItemCount
            val isNotAtTheBeginning = visibleFirstItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE
            val shouldPaginate =  isNotLoadingAndNotLastPage && isLastItemCount && isNotAtTheBeginning && isTotalMoreThanVisible
            if (shouldPaginate){
                viewModel.getPopMovies(apiKey)
                isScrolling = false
            }else{
                popularMoviesRecyclerView.setPadding(0,0,0,0)
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }



    /** to initialize recyclerview */
    private fun initRecyclerView(){

        popularMoviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            val decoration = DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration )
            popularMoviesAdapter = PopularMoviesAdapter()
            adapter = popularMoviesAdapter
            addOnScrollListener(this@PopularMoviesFragment.scrollListener)
            popularMoviesAdapter.setOnItemClickListener(object : PopularMoviesAdapter.onItemClicklistener{
                override fun itemClick(position: Int) {

                   val idP = popularMoviesAdapter.differ.currentList[position].id
                    MoviePreference.myPreference.edit().putInt("id",idP).apply()
                    findNavController().navigate(R.id.action_popularMoviesFragment_to_movieDetailsFragment)
                }

            })

        }
    }



    /** get popular movie from Database */
    fun updateListFromDatabase(){
        viewModel.getPopularMovieLiveDataDb().observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()){
                popularMoviesAdapter.differ.submitList(it)
                Log.d("MQ", "popularMovie saved to the local database: $it")
            }else{
                viewModel.getPopMovies(apiKey)
            }

        })
    }


}