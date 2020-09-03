package com.edmko.cookingbook.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.edmko.cookingbook.db.DatabaseRecipe

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: DatabaseRecipe)

    @Update
    fun updateRecipe(vararg recipe: DatabaseRecipe)

    @Delete
    fun deleteRecipe(vararg recipe: DatabaseRecipe)

    @Query("Delete from recipe")
    fun deleteAllRecipes()

    @Query("Select * from recipe Where id=:id LIMIT 1")
    fun observeRecipeById(vararg id: String): LiveData<DatabaseRecipe>

    @Query("Select * from recipe Where id=:id LIMIT 1")
    fun getRecipeById(vararg id: String): DatabaseRecipe?

    @Query("Select * from recipe")
    fun getAllRecipes(): LiveData<List<DatabaseRecipe>>

    @Query("Select * from recipe WHERE tags LIKE :tag" )
    fun getRecipesByTag(vararg tag: String) : LiveData<List<DatabaseRecipe>>
}