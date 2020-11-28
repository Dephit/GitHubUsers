package com.example.githubuserlistapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class UserListAdapter: RecyclerView.Adapter<UserViewHolder>(){

    private var userList: MutableList<UserInList> = mutableListOf()

    companion object {
        fun initAdapter(_userList: List<UserInList> = listOf()): UserListAdapter {
            val adapter = UserListAdapter()
            adapter.setList(_userList)
            return adapter
        }

        fun initAdapter(bundle: Bundle): UserListAdapter {
            val adapter = UserListAdapter()
            val list = UserInListConverter().stringToSomeObjectList(bundle.getString("user_list"))
            list?.let { adapter.setList(it) }
            return adapter
        }
    }

    fun setList(_userList: List<UserInList>){
        userList = _userList.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    fun getLastItemId(): Int? = userList.lastOrNull()?.id

    fun addList(_userList: List<UserInList>) {
        userList.addAll(_userList)
        notifyDataSetChanged()
    }

    fun saveToState(outState: Bundle) {
        outState.putString("user_list", UserInListConverter().someObjectListToString(userList))
    }

}

class UserInListConverter {

    fun stringToSomeObjectList(d: String?): MutableList<UserInList>? {
        val data = d ?: return Collections.emptyList()
        if (data == "" || data == "null" || data == "[]" || data.isEmpty()) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<UserInList?>?>() {}.type

        return try {
            Gson().fromJson(data, listType)
        }catch (e:Exception){
            Collections.emptyList()
        }
    }

    fun someObjectListToString(someObjects: MutableList<UserInList>?): String? {
        return Gson().toJson(someObjects)
    }
}