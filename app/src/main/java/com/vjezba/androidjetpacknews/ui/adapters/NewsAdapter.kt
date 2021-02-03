package com.vjezba.androidjetpacknews.ui.adapters

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vjezba.androidjetpacknews.R
import com.vjezba.androidjetpacknews.ui.utilities.ListDiffer
import com.vjezba.domain.model.Articles
import kotlinx.android.synthetic.main.news_list.view.*

class NewsAdapter(var articlesList: MutableList<Articles>,
                  val ArticlesClickListener: (Int) -> Unit )
    : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo: ImageView = itemView.imagePhoto
        val layoutParent: LinearLayout = itemView.parentLayout

        val title: TextView = itemView.textTitleName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindItem(holder, articlesList[position], position)
    }

    fun updateDevices(updatedDevices: MutableList<Articles>) {

        val listDiff = ListDiffer.getDiff(
            articlesList,
            updatedDevices,
            { old, new ->
                old.title == new.title &&
                        old.author == new.author &&
                        old.description == new.description &&
                        old.id == new.id &&
                        old.publishedAt == new.publishedAt &&
                        old.url == new.url &&
                        old.urlToImage == new.urlToImage
            })

        for (diff in listDiff) {
            when (diff) {
                is ListDiffer.DiffInserted -> {
                    articlesList.addAll(diff.elements)
                    Log.d("notifyItemRangeInserted", "notifyItemRangeInserted")
                    notifyItemRangeInserted(diff.position, diff.elements.size)
                }
                is ListDiffer.DiffRemoved -> {
                    //remove devices
                    for (i in (articlesList.size - 1) downTo diff.position) {
                        articlesList.removeAt(i)
                    }
                    Log.d("notifyItemRangeRemoved", "notifyItemRangeRemoved")
                    notifyItemRangeRemoved(diff.position, diff.count)
                }
                is ListDiffer.DiffChanged -> {
                    articlesList[diff.position] = diff.newElement
                    Log.d("notifyItemChanged", "notifyItemChanged")
                    notifyItemChanged(diff.position)
                }
            }
        }
    }


    private fun bindItem(holder: ViewHolder, article: Articles, position: Int) {

        Glide.with(holder.itemView)
            .load(article.urlToImage)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.photo)

        holder.title.text = "Name: " + article.title

        holder.layoutParent.setOnClickListener{
            ArticlesClickListener(position)
        }
    }

    override fun getItemCount(): Int {
        return articlesList.size
    }

    fun setItems(data: List<Articles>) {
        Log.d(ContentValues.TAG, "Da li ce uci sim ooo: ${data.joinToString { "-" }}")
        articlesList.addAll(data)
        notifyDataSetChanged()
    }

    fun getItems() = articlesList

}