package com.vjezba.androidjetpackmovie.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.androidjetpackmovie.R
import com.vjezba.androidjetpackmovie.ui.utilities.ListDiffer
import com.vjezba.domain.model.TrailerResult
import kotlinx.android.synthetic.main.trailers_list.view.*

class TrailersAdapter(var TrailerResultList: MutableList<TrailerResult> )
    : RecyclerView.Adapter<TrailersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.textName
        val site: TextView = itemView.textSite
        val size: TextView = itemView.textSize
        val type: TextView = itemView.textType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trailers_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindItem(holder, TrailerResultList[position])
    }

    fun update(items: MutableList<TrailerResult>) {

        val listDiff = ListDiffer.getDiff(
            TrailerResultList,
            items,
            { old, new ->
                old.id == new.id &&
                old.key == new.key &&
                        old.name == new.name &&
                        old.genreIds == new.genreIds &&
                        old.site == new.site &&
                        old.size == new.size
            })

        for (diff in listDiff) {
            when (diff) {
                is ListDiffer.DiffInserted -> {
                    TrailerResultList.addAll(diff.elements)
                    Log.d("notifyItemRangeInserted", "notifyItemRangeInserted")
                    notifyItemRangeInserted(diff.position, diff.elements.size)
                }
                is ListDiffer.DiffRemoved -> {
                    //remove devices
                    for (i in (TrailerResultList.size - 1) downTo diff.position) {
                        TrailerResultList.removeAt(i)
                    }
                    Log.d("notifyItemRangeRemoved", "notifyItemRangeRemoved")
                    notifyItemRangeRemoved(diff.position, diff.count)
                }
                is ListDiffer.DiffChanged -> {
                    TrailerResultList[diff.position] = diff.newElement
                    Log.d("notifyItemChanged", "notifyItemChanged")
                    notifyItemChanged(diff.position)
                }
            }
        }
    }


    private fun bindItem(holder: ViewHolder, article: TrailerResult) {
        holder.title.text = "Name: " + article.name
        holder.site.text = "Site: " + article.site
        holder.size.text = "Size: " + article.size
        holder.type.text = "Type: " + article.type
    }

    override fun getItemCount(): Int {
        return TrailerResultList.size
    }

}