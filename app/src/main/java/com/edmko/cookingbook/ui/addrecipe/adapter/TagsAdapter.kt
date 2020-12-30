package com.edmko.cookingbook.ui.addrecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edmko.cookingbook.R
import com.edmko.cookingbook.utils.OnTagClickListener

import kotlinx.android.synthetic.main.tags_recycler_item.view.*

class TagsAdapter : RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {
    private val data = mutableListOf<String>()
    var onTagClicks : ((Int) -> Unit)? = null
    fun setData(tags: List<String>) {
        data.clear()
        data.addAll(tags)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        holder.tag.text = data[position]
        holder.itemView.setOnClickListener { onTagClicks?.invoke(position) }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        return TagsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.tags_recycler_item, parent, false)
        )
    }

    class TagsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tag: TextView = itemView.tag_item
    }
}
