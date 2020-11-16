package com.edmko.cookingbook.ui.recipes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.edmko.cookingbook.R
import com.edmko.cookingbook.models.Recipe
import com.edmko.cookingbook.utils.OnItemClickListener
import kotlinx.android.synthetic.main.recipe_card.view.*
import java.util.*

class RecipeAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>(), Filterable {

    private var recipeList: MutableList<Recipe> = arrayListOf()
    private var searchableList: MutableList<Recipe> = arrayListOf()
    private var onNothingFound: (() -> Unit)? = null

    fun setData(data: List<Recipe>) {
        recipeList.clear()
        searchableList.clear()
        recipeList.addAll(data.asReversed())
        searchableList.addAll(recipeList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return searchableList.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, pos: Int) {
        holder.recipeName.text = searchableList[pos].name
        holder.recipeAuthor.text = searchableList[pos].author
        holder.itemView.setOnClickListener { listener.onItemClick(searchableList[pos].id) }

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


    override fun getFilter(): Filter {
        return object : Filter() {


            private val filterResult = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchableList.clear()
                if (constraint.isNullOrBlank().not()) {
                    val filterPattern =
                        constraint.toString().toLowerCase(Locale.getDefault()).trim { it <= ' ' }
                    recipeList.forEach {
                        if (it.name.toLowerCase(Locale.getDefault()).contains(filterPattern) ||
                            it.author.toLowerCase(Locale.getDefault()).contains(filterPattern) ||
                            it.tags.contains(filterPattern)
                        )
                            searchableList.add(it)
                    }
                } else searchableList.addAll(recipeList)

                return filterResult.also {result -> result.values = searchableList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (searchableList.isNullOrEmpty())
                    onNothingFound?.invoke()
                notifyDataSetChanged()
            }
        }
    }
}