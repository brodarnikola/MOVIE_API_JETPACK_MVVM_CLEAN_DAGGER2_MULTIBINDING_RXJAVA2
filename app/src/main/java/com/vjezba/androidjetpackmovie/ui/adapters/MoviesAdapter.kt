package com.vjezba.androidjetpackmovie.ui.adapters

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vjezba.androidjetpackmovie.R
import com.vjezba.androidjetpackmovie.ui.utilities.ListDiffer
import com.vjezba.domain.model.MovieResult
import kotlinx.android.synthetic.main.news_list.view.*

class MoviesAdapter(var MovieResultList: MutableList<MovieResult>,
                    val MovieResultClickListener: (Long) -> Unit )
    : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo: ImageView = itemView.imagePhoto
        val layoutParent: ConstraintLayout = itemView.parentLayout

        val title: TextView = itemView.textTitleName
        val description: TextView = itemView.textDescription
        val popularity: TextView = itemView.textPopularity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindItem(holder, MovieResultList[position], position)
    }

    fun updateDevices(updatedDevices: MutableList<MovieResult>) {

        val listDiff = ListDiffer.getDiff(
            MovieResultList,
            updatedDevices,
            { old, new ->
                old.id == new.id &&
                old.adult == new.adult &&
                        old.backdropPath == new.backdropPath &&
                        old.genreIds == new.genreIds &&
                        old.originalLanguage == new.originalLanguage &&
                        old.originalTitle == new.originalTitle &&
                        old.overview == new.overview
            })

        for (diff in listDiff) {
            when (diff) {
                is ListDiffer.DiffInserted -> {
                    MovieResultList.addAll(diff.elements)
                    Log.d("notifyItemRangeInserted", "notifyItemRangeInserted")
                    notifyItemRangeInserted(diff.position, diff.elements.size)
                }
                is ListDiffer.DiffRemoved -> {
                    //remove devices
                    for (i in (MovieResultList.size - 1) downTo diff.position) {
                        MovieResultList.removeAt(i)
                    }
                    Log.d("notifyItemRangeRemoved", "notifyItemRangeRemoved")
                    notifyItemRangeRemoved(diff.position, diff.count)
                }
                is ListDiffer.DiffChanged -> {
                    MovieResultList[diff.position] = diff.newElement
                    Log.d("notifyItemChanged", "notifyItemChanged")
                    notifyItemChanged(diff.position)
                }
            }
        }
    }


    private fun bindItem(holder: ViewHolder, article: MovieResult, position: Int) {

        Glide.with(holder.itemView)
            .load( "https://image.tmdb.org/t/p/w500/" + article.backdropPath)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.photo)

        holder.title.text = "Name: " + article.originalTitle
        holder.description.text = "Description: " + article.overview
        holder.popularity.text = "Popularity: " + article.popularity

        holder.layoutParent.setOnClickListener{
            article.id?.let { it -> MovieResultClickListener(it) }
        }
    }

    override fun getItemCount(): Int {
        return MovieResultList.size
    }

    fun setItems(data: List<MovieResult>) {
        Log.d(ContentValues.TAG, "Da li ce uci sim ooo: ${data.joinToString { "-" }}")
        MovieResultList.addAll(data)
        notifyDataSetChanged()
    }

    fun getItems() = MovieResultList

}