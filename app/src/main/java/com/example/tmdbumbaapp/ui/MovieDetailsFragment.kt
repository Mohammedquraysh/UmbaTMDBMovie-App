package com.example.tmdbumbaapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tmdbumbaapp.R
import com.example.tmdbumbaapp.apiKey
import com.example.tmdbumbaapp.databinding.FragmentMovieDetailsBinding
import com.example.tmdbumbaapp.model.moviedetails.MovieDetailsResponse
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.example.tmdbumbaapp.repository.Resource
import com.example.tmdbumbaapp.util.MoviePreference
import com.example.tmdbumbaapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private lateinit var _binding: FragmentMovieDetailsBinding
    private val binding get() = _binding
    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMovieDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newId =   MoviePreference.myPreference.getInt("id",1)
        val id = newId
        makeApiCall()
        viewModel.movieDetail(id, apiKey)

    }



    /** binding each movie data to get Movies details */
    private fun bindUi(it: Results){
        binding.tvTitlePopularMovieDetails.text = it.title
        binding.tvPopularityPopularMovieDetails.text = it.popularity.toString()
        binding.tvReleasedDatePopularMovieDetails.text = it.release_date
        binding.tvRatingPopularMovieDetails.text = it.original_language
        binding.tvOverviewMovie.text = getString(R.string.overview, "${it.overview}")
        binding.tvStatusPopularMovieDetails.text = it.vote_average.toString()

        val image = binding.ivMovieImage
        Glide.with(image.context)
            .load("https://image.tmdb.org/t/p/w500${it.backdrop_path}")
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(RequestOptions().placeholder(R.drawable.bruno_2))
            .into(image)
    }


    /** observe the APi call for Movies details */
    fun makeApiCall(){
        Log.d("MQ","Movies detail")
        viewModel.movieDetailLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is  Resource.Success -> {
                    it.data.let {
                        bindUi(it!!)
                    }
                }

                is Resource.Error -> {
                    it.message.let {
                        Log.d("MQ", "An error occur: $it")
                    }
                }
                is Resource.Loading -> {

                }

            }

        })


    }
}