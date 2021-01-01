package com.edmko.cookingbook.ui.recipes.adapter

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.edmko.cookingbook.R
import com.edmko.cookingbook.models.Recipe
import kotlinx.android.synthetic.main.recipe_card.view.*
import java.util.*

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipeList: List<Recipe> = emptyList()
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
            recipeList.filter { recipe ->
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

    override fun getItemCount() = searchableList.size


    override fun onBindViewHolder(holder: RecipeViewHolder, pos: Int) {
        holder.itemView.apply {
            tvName.text = searchableList[pos].name
            tvAuthor.text = searchableList[pos].author
            setOnClickListener { onItemClick?.invoke(searchableList[pos].id) }
            Glide.with(context)
                .load(searchableList[pos].image)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
                .placeholder(R.drawable.ic_insert_photo_24px)
                .into(imgPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecipeViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.recipe_card, parent, false)
    )


    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}