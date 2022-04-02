package com.example.tmdbumbaapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbumbaapp.R
import com.example.tmdbumbaapp.adapter.LatestMovieAdapter
import com.example.tmdbumbaapp.apiKey
import com.example.tmdbumbaapp.application.TMDBApplication
import com.example.tmdbumbaapp.databinding.FragmentLatestMoviesBinding
import com.example.tmdbumbaapp.model.LatestMovieResponseModel
import com.example.tmdbumbaapp.repository.Resource
import com.example.tmdbumbaapp.util.ApiCallNetworkResource
import com.example.tmdbumbaapp.util.ConnectivityLiveData
import com.example.tmdbumbaapp.util.MoviePreference
import com.example.tmdbumbaapp.util.observeNetworkConnection
import com.example.tmdbumbaapp.viewmodel.MoviesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LatestMoviesFragment : Fragment() {
    private lateinit var _binding: FragmentLatestMoviesBinding
    private val binding get() = _binding
    private lateinit var latestMoviesAdapter:LatestMovieAdapter
    private lateinit var latestMoviesRecyclerView: RecyclerView
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var connectivityLiveData: ConnectivityLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityLiveData = ConnectivityLiveData(TMDBApplication.application)
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLatestMoviesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        latestMoviesRecyclerView = binding.latestMoviesRecyclerview
        initRecyclerView()



        /** observing connectivity */
        connectivityLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    observeApiCall()
                    viewModel.latestMovie(apiKey)

                    Snackbar.make(binding.root, "Network is available", Snackbar.LENGTH_LONG).show()
                }
               false -> {
                   updateListFromDatabase()
                   Log.d("OMQ", updateListFromDatabase().toString())
                   Snackbar.make(binding.root, "Network is not available", Snackbar.LENGTH_LONG).show()

               }
            }
        })

    }


    /** initialise recyclerview*/
    private fun initRecyclerView(){

        latestMoviesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            latestMoviesAdapter = LatestMovieAdapter()
            adapter = latestMoviesAdapter
            latestMoviesAdapter.setOnItemClickListener(object : LatestMovieAdapter.onItemClicklistener{
                override fun itemClick(position: Int) {
                    val idP = latestMoviesAdapter.differ.currentList[position].id
                    MoviePreference.myPreference.edit().putInt("id",idP).apply()
                    findNavController().navigate(R.id.action_latestMoviesFragment_to_movieDetailsFragment)
                }

            })

        }
    }



    /** observe API call for latest movie*/
    private fun observeApiCall() {
        Log.d("MQ", "Welcome to tmdb mobile app")
        viewModel.latestMovieLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    hideProgressbar()
                    Log.d("MQ", it.toString())
                    it.data.let {
                        latestMoviesAdapter.setData(it!!)
                        Log.d("MQ",latestMoviesAdapter.setData(it!!).toString() )
                        viewModel.saveLatestMovieToDb(it)

                    }

                }

                is Resource.Error -> {
                    hideProgressbar()
                    it.message.let {
                        Log.d("MQ", "An error occur: $it")
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }

                }

                is Resource.Loading -> {
                    showProgressbar()

                }
            }
        }

    }



    fun hideProgressbar(){
        binding.latestMovieProgressBar.visibility = View.GONE

    }

    fun showProgressbar(){
        binding.latestMovieProgressBar.visibility = View.VISIBLE

    }


    /** get latest movie from Database */
    fun updateListFromDatabase(){
        viewModel.getLatestMovieLiveDataDb().observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()){
                latestMoviesAdapter.differ.submitList(it)
                Log.d("MM", it.toString())
            }else{
                viewModel.latestMovie(apiKey)
            }
            Log.d("MQs", "Latest movie saved to the local database: $it")
        })
    }


}