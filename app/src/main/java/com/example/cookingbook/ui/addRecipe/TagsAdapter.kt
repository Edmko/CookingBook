package com.example.cookingbook.ui.addRecipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookingbook.R
import com.example.cookingbook.utils.OnTagClickListener
import kotlinx.android.synthetic.main.tags_recycler_item.view.*

class TagsAdapter(
    private val data: List<String>,
    private val listener : OnTagClickListener
) : RecyclerView.Adapter<TagsAdapter.TagsViewHolder>(){

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: TagsAdapter.TagsViewHolder, position: Int) {
        holder.tag.text = data[position]
        holder.itemView.setOnClickListener { listener.onItemClick(position) }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsAdapter.TagsViewHolder {
        return TagsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.tags_recycler_item, parent, false)
        )
    }

    inner class TagsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tag: TextView = itemView.tag_item
    }
}
