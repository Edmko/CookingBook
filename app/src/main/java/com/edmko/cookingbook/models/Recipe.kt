package com.edmko.cookingbook.models

import com.edmko.cookingbook.db.DatabaseRecipe
import com.edmko.cookingbook.db.TypeConverter

data class Recipe(
    var id: String,
    var image: String = "",
    var name: String = "",
    var author: String = "",
    var notes: String = "",
    var ingredients: MutableList<Pair<String, String>> = mutableListOf(),
    var tags: MutableList<String> = mutableListOf(),
    var link: String = ""
)

fun Recipe.asDatabaseRecipe(): DatabaseRecipe {
    return DatabaseRecipe(
        id = this.id,
        image = this.image,
        name = this.name,
        author = this.author,
        notes = this.notes,
        tags = TypeConverter.listToString(this.tags),
        ingredients = TypeConverter.listOfPairsToString(this.ingredients),
        link = this.link
    )
}