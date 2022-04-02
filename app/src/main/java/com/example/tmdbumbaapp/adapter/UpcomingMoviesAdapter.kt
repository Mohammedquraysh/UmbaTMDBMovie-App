package com.example.tmdbumbaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tmdbumbaapp.R
import com.example.tmdbumbaapp.databinding.UpcomingMovieLayoutAdapterBinding
import com.example.tmdbumbaapp.model.upcomingmoviemodel.Result

class UpcomingMoviesAdapter(): RecyclerView.Adapter<UpcomingMoviesAdapter.UpcomingMovieViewHolder>() {

    private lateinit var listener: onItemClicklistener

    interface onItemClicklistener{
        fun itemClick(position: Int)
    }


    fun setOnItemClickListener(mListener: onItemClicklistener){
        listener = mListener
    }


    val diffUtilCallBack = object : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.original_title == newItem.original_title
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.original_title == newItem.original_title && oldItem.backdrop_path == newItem.backdrop_path
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallBack)
    inner class UpcomingMovieViewHolder(binding: UpcomingMovieLayoutAdapterBinding,listener: onItemClicklistener): RecyclerView.ViewHolder(binding.root){
        val image = binding.ivUpcomingMovieImage
        val title = binding.tvTitleUpcomingMovie
        val description = binding.tvDescriptionUpcomingMovie

        fun bindView(popularResults: Result){
            //this.image = latestMovie
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500${popularResults.backdrop_path}")
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions().placeholder(R.drawable.bruno_2))
                .into(image)
            title.text = popularResults.title
            description.text = popularResults.overview

        }


        init {
            itemView.setOnClickListener {
                listener.itemClick(adapterPosition)
            }

        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingMovieViewHolder {
        val binding = UpcomingMovieLayoutAdapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UpcomingMovieViewHolder(binding,listener)

    }

    override fun onBindViewHolder(holder: UpcomingMovieViewHolder, position: Int) {
        holder.bindView(differ.currentList[position])
    }


    override fun getItemCount(): Int {
        return differ.currentList.size

    }

}