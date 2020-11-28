package com.example.githubuserlistapp.converters

import com.example.githubuserlistapp.data.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class UserInListConverter {

    fun stringToSomeObjectList(d: String?): MutableList<User>? {
        val data = d ?: return Collections.emptyList()
        if (data == "" || data == "null" || data == "[]" || data.isEmpty()) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<User?>?>() {}.type

        return try {
            Gson().fromJson(data, listType)
        }catch (e:Exception){
            Collections.emptyList()
        }
    }

    fun someObjectListToString(someObjects: MutableList<User>?): String? {
        return Gson().toJson(someObjects)
    }
}