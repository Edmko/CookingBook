package com.example.cookingbook.ui.main

import android.content.Context
import android.view.*
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookingbook.R
import com.example.cookingbook.db.DatabaseRecipe
import com.example.cookingbook.models.Recipe
import kotlinx.android.synthetic.main.recipe_card.view.*
import java.util.*

class RecipeAdapter(
    private val context: Context,
    private val data: List<Recipe>,
    private val view: View,
    private val state: Boolean
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>(), Filterable {
    var searchableList: MutableList<Recipe> = arrayListOf()
    private var onNothingFound: (() -> Unit)? = null
    init {
        searchableList.addAll(data)
    }
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        searchableList.apply {
            holder.recipeName.text = this[position].name
            holder.recipeAuthor.text = this[position].author
            if (this[position].image != "") {
                Glide.with(holder.itemView.context)
                    .load(this[position].image)
                    .into(holder.image)
            } else holder.image.setImageResource(R.drawable.ic_insert_photo_24px)
            holder.itemView.setOnClickListener {
                val action =
                    MainFragmentDirections.actionMainFragmentToRecipeFragment(this[position].id)
                findNavController(view).navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return searchableList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return (if (state) {
            RecipeViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.recipe_card_v, parent, false
                )
            )
        } else RecipeViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recipe_card, parent, false
            )
        ))
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.image
        val recipeName: TextView = itemView.recipe_name
        val recipeAuthor: TextView = itemView.recipe_author
    }

    override fun getFilter(): Filter {
        return object : Filter() {


            private val filterResult = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchableList.clear()
                if (constraint.isNullOrBlank()) {
                    searchableList.addAll(data)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.getDefault()).trim { it <= ' ' }
                        data.forEach{
                        if (it.name.toLowerCase(Locale.getDefault()).contains(filterPattern)||
                            it.author.toLowerCase(Locale.getDefault()).contains(filterPattern)||
                            it.tags.contains(filterPattern))
                            searchableList.add(it)
                        }
                }

                return filterResult.also { it.values = searchableList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (searchableList.isNullOrEmpty())
                    onNothingFound?.invoke()
                notifyDataSetChanged()
            }
        }
    }
}