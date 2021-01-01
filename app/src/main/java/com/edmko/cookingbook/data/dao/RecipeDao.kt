package com.edmko.cookingbook.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.edmko.cookingbook.data.DatabaseRecipe

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: DatabaseRecipe)

    @Update
    suspend fun updateRecipe(vararg recipe: DatabaseRecipe)

    @Delete
    suspend fun deleteRecipe(vararg recipe: DatabaseRecipe)

    @Query("DELETE FROM recipe WHERE id = :id")
    suspend fun deleteById(id:String)

    @Query("Delete from recipe")
    suspend fun deleteAllRecipes()

    @Query("Select * from recipe Where id=:id LIMIT 1")
    fun observeRecipeById(vararg id: String): LiveData<DatabaseRecipe>

    @Query("Select * from recipe Where id=:id LIMIT 1")
    suspend fun getRecipeById(vararg id: String): DatabaseRecipe?

    @Query("Select * from recipe")
    fun observeAllRecipes(): LiveData<List<DatabaseRecipe>>

    @Query("Select * from recipe WHERE tags LIKE :tag" )
    fun getRecipesByTag(vararg tag: String) : LiveData<List<DatabaseRecipe>>
}