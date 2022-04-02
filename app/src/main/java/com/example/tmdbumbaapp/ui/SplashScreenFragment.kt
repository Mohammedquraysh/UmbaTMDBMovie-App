package com.example.tmdbumbaapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tmdbumbaapp.R
import com.example.tmdbumbaapp.databinding.FragmentSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {
   private lateinit var _binding: FragmentSplashScreenBinding
   private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        /** applying animation to the splash screen*/
        val tmdbMovieAnimation = android.view.animation.AnimationUtils.loadAnimation(
            requireContext(), R.anim.slide_from_left_animation
        )
        binding.octamileLogo.startAnimation(tmdbMovieAnimation)

        /** delay splash screen for two and half seconds*/
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            view.post {
                findNavController().navigate(R.id.action_splashScreenFragment_to_latestMoviesFragment)
            }

        }, 2500)
    }


}