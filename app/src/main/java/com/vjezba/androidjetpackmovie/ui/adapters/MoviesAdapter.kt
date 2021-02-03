package com.vjezba.androidjetpackmovie.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vjezba.androidjetpackmovie.R
import com.vjezba.androidjetpackmovie.ui.utilities.ListDiffer
import com.vjezba.domain.model.MovieResult
import kotlinx.android.synthetic.main.movies_list.view.*

class MoviesAdapter(
    var movieResultList: MutableList<MovieResult>,
    val MovieResultClickListener: (Long) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var isLoaderVisible = false

    enum class ITEM_TYPES(val typeValue: Int) {
        VIEW_TYPE_NORMAL(0),
        VIEW_TYPE_LOADING(1);

        companion object {
            fun from(findViewByIdValue: Int): ITEM_TYPES =
                values().first { it.typeValue == findViewByIdValue }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == movieResultList.size - 1) ITEM_TYPES.VIEW_TYPE_LOADING.typeValue else ITEM_TYPES.VIEW_TYPE_NORMAL.typeValue
        } else {
            ITEM_TYPES.VIEW_TYPE_NORMAL.typeValue
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = movieResultList[position]
        when (viewType.showProgressBar) {
            true -> {
                holder as ProgressHolder
            }
            else -> {

                holder as MovieViewHolder
                holder.bindItem( holder, movieResultList[position] )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == 0) {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.movies_list, parent, false)
            return MovieViewHolder(itemView)
        } else {
            val itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_list, parent, false)
            return ProgressHolder(itemView)
        }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ProgressHolder(itemView: View) : ViewHolder(itemView) {
    }

    inner class MovieViewHolder(itemView: View) : ViewHolder(itemView) {
        val photo: ImageView = itemView.imagePhoto
        val layoutParent: ConstraintLayout = itemView.parentLayout

        val title: TextView = itemView.textTitleName
        val description: TextView = itemView.textDescription
        val popularity: TextView = itemView.textPopularity

        fun bindItem(holder: ViewHolder, article: MovieResult) {

            Glide.with(holder.itemView)
                .load("https://image.tmdb.org/t/p/w500/" + article.backdropPath)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photo)

            title.text = "Name: " + article.originalTitle
            description.text = "Description: " + article.overview
            popularity.text = "Popularity: " + article.popularity

            layoutParent.setOnClickListener {
                article.id?.let { it -> MovieResultClickListener(it) }
            }
        }
    }

    override fun getItemCount(): Int {
        return movieResultList.size
    }


    fun addLoading() {
        isLoaderVisible = true
        val deliverParcel = MovieResult()
        deliverParcel.showProgressBar = true
        movieResultList.add(deliverParcel)
        notifyItemInserted(movieResultList.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position: Int = movieResultList.size - 1
        val item = movieResultList[position]
        if (item != null) {
            movieResultList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateDevices(updatedDevices: MutableList<MovieResult>) {
        movieResultList.addAll(updatedDevices)
        notifyItemRangeInserted(movieResultList.size, updatedDevices.size)
    }


}