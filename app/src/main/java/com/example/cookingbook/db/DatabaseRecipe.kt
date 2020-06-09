package com.example.cookingbook.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cookingbook.models.Recipe

@Entity (tableName = "recipe")
data class DatabaseRecipe(
    @PrimaryKey val id : String,
    @ColumnInfo(name = "image") var image : String,
    @ColumnInfo (name="name") var name : String,
    @ColumnInfo (name="author")var author : String,
    @ColumnInfo (name="notes")var notes : String,
    @ColumnInfo (name="ingredients")var ingredients:String,
    @ColumnInfo (name ="tags")var tags : String
)

fun List<DatabaseRecipe>.asDomainRecipe():List<Recipe>{
    return map{
        Recipe(id=it.id,
        image = it.image,
        name = it.name,
        author = it.author,
        notes = it.notes,
        tags = TypeConverter.stringToList(it.tags)?: mutableListOf(),
        ingredients = TypeConverter.stringToListOfPairs(it.ingredients))
    }
}
fun DatabaseRecipe.asDomainRecipe(): Recipe {
    return Recipe(
        id=this.id,
        image = this.image,
        name = this.name,
        author = this.author,
        notes = this.notes,
        tags = TypeConverter.stringToList(this.tags)?: mutableListOf(),
        ingredients = TypeConverter.stringToListOfPairs(this.ingredients)
    )
}