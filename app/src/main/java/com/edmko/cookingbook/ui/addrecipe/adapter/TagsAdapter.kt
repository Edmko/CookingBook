package com.edmko.cookingbook.ui.addrecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edmko.cookingbook.R

import kotlinx.android.synthetic.main.tags_recycler_item.view.*

class TagsAdapter : RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {

    private val data = mutableListOf<String>()

    var onTagClicks : ((Int) -> Unit)? = null

    fun setData(tags: List<String>) {
        data.clear()
        data.addAll(tags)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        holder.itemView.apply {
        tvTitle.text = data[position]
        setOnClickListener { onTagClicks?.invoke(position) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        return TagsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.tags_recycler_item, parent, false)
        )
    }

    class TagsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
