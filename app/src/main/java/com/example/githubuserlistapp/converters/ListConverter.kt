package com.example.githubuserlistapp.converters

import com.example.githubuserlistapp.data.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class ListConverter<T>() {

    //Converts string to defined object list
    fun stringToObjectList(d: String?, listType: Type = object : TypeToken<List<T>>() {}.type): MutableList<T>? {
        //returns empty list if passed string is null
        val data = d ?: return Collections.emptyList()
        //val listType: Type = object : TypeToken<List<T>>() {}.type
        return try {
            Gson().fromJson(data, listType)
        }catch (e:Exception){
            Collections.emptyList()
        }
    }
    //Converts defined object list to string
    fun objectListToString(someObjects: MutableList<T>?): String? {
        return Gson().toJson(someObjects)
    }
}