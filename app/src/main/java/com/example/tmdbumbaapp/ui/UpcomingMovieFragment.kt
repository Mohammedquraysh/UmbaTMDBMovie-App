package com.example.tmdbumbaapp.ui

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbumbaapp.QUERY_PAGE
import com.example.tmdbumbaapp.R
import com.example.tmdbumbaapp.adapter.PopularMoviesAdapter
import com.example.tmdbumbaapp.adapter.UpcomingMoviesAdapter
import com.example.tmdbumbaapp.apiKey
import com.example.tmdbumbaapp.application.TMDBApplication
import com.example.tmdbumbaapp.databinding.FragmentUpcomingMovieBinding
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.example.tmdbumbaapp.model.upcomingmoviemodel.Result
import com.example.tmdbumbaapp.repository.Resource
import com.example.tmdbumbaapp.util.ConnectivityLiveData
import com.example.tmdbumbaapp.util.MoviePreference
import com.example.tmdbumbaapp.viewmodel.MoviesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingMovieFragment : Fragment() {
    private lateinit var _binding: FragmentUpcomingMovieBinding
    private val binding get() = _binding
    private lateinit var upcomingMoviesAdapter: UpcomingMoviesAdapter
    private lateinit var upcomingMoviesRecyclerView: RecyclerView
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var connectivityLiveData: ConnectivityLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityLiveData = ConnectivityLiveData(TMDBApplication.application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUpcomingMovieBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upcomingMoviesRecyclerView = binding.upcomingMoviesRecyclerview
        initRecyclerView()

       /** observing network state */
        connectivityLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    /** observe api call for upcoming movies */
                    viewModel.upcomingMovieLiveData.observe(viewLifecycleOwner, Observer {
                        when(it){
                            is Resource.Success -> {
                                hideProgressbar()
                                it.data?.let {
                                    upcomingMoviesAdapter.differ.submitList(it.results.toList())
                                    val totalPages = it.total_pages / QUERY_PAGE + 2
                                    isLastPage =viewModel.pageNumber == totalPages
                                    viewModel.saveUpcomingMovieToDb(it.results)
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


        updateListFromDatabase()

    }



    fun hideProgressbar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = false
    }

    fun showProgressbar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }


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

                viewModel.getUpcomingMovies(apiKey)
                isScrolling = false
            }else{
                upcomingMoviesRecyclerView.setPadding(0,0,0,0)
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
    private fun initRecyclerView() {
        upcomingMoviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            upcomingMoviesAdapter = UpcomingMoviesAdapter()
            adapter = upcomingMoviesAdapter
            addOnScrollListener(this@UpcomingMovieFragment.scrollListener)
            upcomingMoviesAdapter.setOnItemClickListener(object : UpcomingMoviesAdapter.onItemClicklistener{
                override fun itemClick(position: Int) {

                    val idP = upcomingMoviesAdapter.differ.currentList[position].id
                    MoviePreference.myPreference.edit().putInt("id",idP).apply()
                    findNavController().navigate(R.id.action_upcomingMovieFragment_to_movieDetailsFragment)
                }

            })

        }
    }




    /** get upcoming movie from Database */
    fun updateListFromDatabase(){
        viewModel.getUpcomingMovieFromDb().observe(viewLifecycleOwner, Observer {
            upcomingMoviesAdapter.differ.submitList(it)
            Log.d("MQ", "popularMovie saved to the local database: $it")
        })
    }


}