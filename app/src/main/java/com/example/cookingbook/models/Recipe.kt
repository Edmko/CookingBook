package com.example.cookingbook.models

import com.example.cookingbook.db.DatabaseRecipe
import com.example.cookingbook.db.TypeConverter

data class Recipe(
    var id : String,
    var image : String,
   var  name : String,
    var author : String,
    var notes : String,
   var ingredients:MutableList<Pair<String,String>>,
    var tags : MutableList<String>
)

fun Recipe.asDatabaseRecipe(): DatabaseRecipe {
    return DatabaseRecipe(
        id=this.id,
        image = this.image,
        name = this.name,
        author = this.author,
        notes = this.notes,
        tags = TypeConverter.listToString(this.tags),
        ingredients = TypeConverter.listOfPairsToString(this.ingredients)
    )
}