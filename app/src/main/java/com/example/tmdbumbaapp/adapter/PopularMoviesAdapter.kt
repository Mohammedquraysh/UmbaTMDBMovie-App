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
import com.example.tmdbumbaapp.databinding.PopularMoviesLayoutAdapterBinding
import com.example.tmdbumbaapp.model.popularmoviemodel.Results

class PopularMoviesAdapter(): RecyclerView.Adapter<PopularMoviesAdapter.PopularMovieViewHolder>() {


    private lateinit var listener: onItemClicklistener

    interface onItemClicklistener{
        fun itemClick(position: Int)
    }


    fun setOnItemClickListener(mListener: onItemClicklistener){
        listener = mListener
    }




    val diffUtilCallBack = object : DiffUtil.ItemCallback<Results>() {

        override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.original_title == newItem.original_title
        }

        override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.original_title == newItem.original_title && oldItem.backdrop_path == newItem.backdrop_path
        }
    }

    val differ = AsyncListDiffer(this@PopularMoviesAdapter, diffUtilCallBack)

    inner class PopularMovieViewHolder(binding: PopularMoviesLayoutAdapterBinding, listener: onItemClicklistener): RecyclerView.ViewHolder(binding.root) {
        val image = binding.ivPopularMovieImage
        val title = binding.tvTitlePopularMovie
        val description = binding.tvDescriptionPopularMovie

        fun bindView(popularResults: Results) {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        val binding = PopularMoviesLayoutAdapterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PopularMovieViewHolder(binding,listener)

    }

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        holder.bindView(differ.currentList[position])
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}