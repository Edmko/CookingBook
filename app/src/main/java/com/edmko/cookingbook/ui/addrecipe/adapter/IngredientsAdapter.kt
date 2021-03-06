package com.edmko.cookingbook.ui.addrecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edmko.cookingbook.R
import kotlinx.android.synthetic.main.ingredient_card.view.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {
    private val data= mutableListOf<Pair<String, String>>()

    fun setData(ingredients: List<Pair<String, String>>){
        data.clear()
        data.addAll(ingredients)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.ingredient.text = data[position].first
        holder.value.text = data[position].second

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        return IngredientsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ingredient_card, parent, false
            )
        )
    }

    inner class IngredientsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredient: TextView = itemView.ingredient
        val value: TextView = itemView.value

    }
}
