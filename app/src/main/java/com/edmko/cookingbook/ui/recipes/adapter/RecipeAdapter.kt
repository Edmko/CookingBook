package com.edmko.cookingbook.ui.recipes.adapter

import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.edmko.cookingbook.R
import com.edmko.cookingbook.models.Recipe
import kotlinx.android.synthetic.main.recipe_card.view.*
import java.util.*

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipeList: List<Recipe> = arrayListOf()
    private var searchableList: MutableList<Recipe> = arrayListOf()
    private var onNothingFound: (() -> Unit)? = null
    var onItemClick: ((String) -> Unit)? = null

    fun setData(data: List<Recipe>) {
        searchableList.clear()
        recipeList = data.asReversed()
        searchableList.addAll(recipeList)
        notifyDataSetChanged()
    }

    fun filter(filter: Editable?) {
        searchableList.clear()
        searchableList = if (filter.isNullOrBlank().not()) {
            val filterPattern =
                filter.toString().toLowerCase(Locale.getDefault()).trim { it <= ' ' }
            recipeList.filter {recipe ->
                Log.d("TAGS", "tags = ${recipe.tags} + ${recipe.name} + $filterPattern")
                recipe.author.toLowerCase(Locale.getDefault()).contains(filterPattern)
                        || recipe.tags.contains(filterPattern)
                        || recipe.name.toLowerCase(Locale.getDefault()).contains(filterPattern)
            }.toMutableList()
        } else recipeList.toMutableList()

        if (searchableList.isEmpty()) {
            onNothingFound?.invoke()
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return searchableList.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, pos: Int) {
        holder.recipeName.text = searchableList[pos].name
        holder.recipeAuthor.text = searchableList[pos].author
        holder.itemView.setOnClickListener { onItemClick?.invoke(searchableList[pos].id) }

        if (searchableList[pos].image.isBlank().not()) {
            Glide.with(holder.itemView.context)
                .load(searchableList[pos].image)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                .into(holder.image)
        } else holder.image.setImageResource(R.drawable.ic_insert_photo_24px)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recipe_card, parent, false
            )
        )
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.image
        val recipeName: TextView = itemView.recipe_name
        val recipeAuthor: TextView = itemView.recipe_author
    }
}