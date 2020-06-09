package com.example.cookingbook.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TypeConverter {
    @TypeConverter
    @JvmStatic
    fun stringToList(value: String): MutableList<String>? {
        return Gson().fromJson(value, object :  TypeToken<MutableList<String>>() {}.type)
    }

    @TypeConverter
    @JvmStatic
    fun listToString(value: MutableList<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    @JvmStatic
    fun stringToListOfPairs(value: String):MutableList<Pair<String, String>>{
        return  Gson().fromJson(value, object : TypeToken<List<Pair<String,String>>>(){}.type)
    }
    @TypeConverter
    @JvmStatic
    fun listOfPairsToString(value : MutableList<Pair<String, String>>):String {
        return Gson().toJson(value)
    }
}