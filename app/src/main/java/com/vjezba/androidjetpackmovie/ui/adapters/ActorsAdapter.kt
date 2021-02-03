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
import com.vjezba.domain.model.ActorsResult
import kotlinx.android.synthetic.main.actors_list.view.*

class ActorsAdapter(var ActorsResultList: MutableList<ActorsResult> )
    : RecyclerView.Adapter<ActorsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.textName
        val knownForDepartment: TextView = itemView.tvKnownForDepartment
        val character: TextView = itemView.tvCharacter
        val gender: TextView = itemView.tvGender
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.actors_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindItem(holder, ActorsResultList[position])
    }

    fun update(items: MutableList<ActorsResult>) {

        val listDiff = ListDiffer.getDiff(
            ActorsResultList,
            items,
            { old, new ->
                old.id == new.id &&
                old.gender == new.gender &&
                        old.name == new.name &&
                        old.character == new.character &&
                        old.knownForDepartment == new.knownForDepartment
            })

        for (diff in listDiff) {
            when (diff) {
                is ListDiffer.DiffInserted -> {
                    ActorsResultList.addAll(diff.elements)
                    Log.d("notifyItemRangeInserted", "notifyItemRangeInserted")
                    notifyItemRangeInserted(diff.position, diff.elements.size)
                }
                is ListDiffer.DiffRemoved -> {
                    //remove devices
                    for (i in (ActorsResultList.size - 1) downTo diff.position) {
                        ActorsResultList.removeAt(i)
                    }
                    Log.d("notifyItemRangeRemoved", "notifyItemRangeRemoved")
                    notifyItemRangeRemoved(diff.position, diff.count)
                }
                is ListDiffer.DiffChanged -> {
                    ActorsResultList[diff.position] = diff.newElement
                    Log.d("notifyItemChanged", "notifyItemChanged")
                    notifyItemChanged(diff.position)
                }
            }
        }
    }


    private fun bindItem(holder: ViewHolder, actor: ActorsResult) {
        holder.title.text = "Name: " + actor.name
        holder.knownForDepartment.text = "Known for department: " + actor.knownForDepartment
        holder.character.text = "Character: " + actor.character
        holder.gender.text = "Gender:" + if( actor.gender == 1 ) "Female" else "Male"
    }

    override fun getItemCount(): Int {
        return ActorsResultList.size
    }

}