package com.example.cookingbook.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookingbook.R
import com.example.cookingbook.db.DatabaseRecipe
import kotlinx.android.synthetic.main.recipe_card.view.*

class RecipeAdapterVertical(
    private val context: Context,
    private val data: List<DatabaseRecipe>,
    private val view: View
) : RecyclerView.Adapter<RecipeAdapterVertical.RecipeViewHolder>() {

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {

        holder.recipeName.text = data[position].name
        holder.recipeAuthor.text = data[position].author
        if (data[position].image != "") {
            Glide.with(holder.itemView.context)
                .load(data[position].image)
                .into(holder.image)
        } else holder.image.setImageResource(R.drawable.ic_insert_photo_24px)
        holder.itemView.setOnClickListener {
            val action =
                MainFragmentDirections.actionMainFragmentToRecipeFragment(data[position].id)
            Navigation.findNavController(view).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recipe_card, parent, false
            )
        )
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.image
        val recipeName: TextView = itemView.recipe_name
        val recipeAuthor: TextView = itemView.recipe_author
    }


}