package com.example.tmdbumbaapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tmdbumbaapp.R
import com.example.tmdbumbaapp.databinding.ActivityMainBinding
import com.example.tmdbumbaapp.databinding.LatestMovieAdapterLayoutBinding
import com.example.tmdbumbaapp.model.LatestMovieResponseModel
import com.example.tmdbumbaapp.model.popularmoviemodel.Results

class LatestMovieAdapter(): RecyclerView.Adapter<LatestMovieAdapter.LatestMovieViewHolder>() {
    private lateinit var listener: onItemClicklistener

    interface onItemClicklistener{
        fun itemClick(position: Int)
    }


    fun setOnItemClickListener(mListener: onItemClicklistener){
        listener = mListener
    }


    val diffUtilCallBack = object : DiffUtil.ItemCallback<LatestMovieResponseModel>() {

        override fun areItemsTheSame(oldItem: LatestMovieResponseModel, newItem: LatestMovieResponseModel): Boolean {
            return oldItem.original_title == newItem.original_title
        }

        override fun areContentsTheSame(oldItem: LatestMovieResponseModel, newItem: LatestMovieResponseModel): Boolean {
            return oldItem.original_title == newItem.original_title && oldItem.backdrop_path == newItem.backdrop_path
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallBack)

    inner class LatestMovieViewHolder(binding: LatestMovieAdapterLayoutBinding, listener: onItemClicklistener):RecyclerView.ViewHolder(binding.root){
        private val image = binding.ivLatestMovieImage
        private val title = binding.tvTitleLatestMovie
        private val description = binding.tvDescriptionLatestMovie

        fun bindView(latestMovie: LatestMovieResponseModel){
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500${latestMovie.backdrop_path}")
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions().placeholder(R.drawable.bruno_2))
                .into(image)
            title.text = latestMovie.title
            description.text = latestMovie.overview

        }


        init {
            itemView.setOnClickListener {
                listener.itemClick(adapterPosition)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestMovieViewHolder {
        val binding = LatestMovieAdapterLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LatestMovieViewHolder(binding,listener)

    }

    override fun onBindViewHolder(holder: LatestMovieViewHolder, position: Int) {
        holder.bindView(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }



    fun setData(data: LatestMovieResponseModel) {
      this.differ.submitList(listOf(data))


    }
}