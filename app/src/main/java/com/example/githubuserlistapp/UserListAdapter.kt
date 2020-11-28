package com.example.githubuserlistapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class UserListAdapter: RecyclerView.Adapter<UserViewHolder>(){

    private var userList: MutableList<User> = mutableListOf()
    lateinit var fragmentCallback: FragmentCallback

    companion object {
        fun initAdapter(_userList: List<User> = listOf(), fragmentCallback: FragmentCallback): UserListAdapter {
            val adapter = UserListAdapter()
            adapter.setList(_userList)
            adapter.setCallback(fragmentCallback)
            return adapter
        }

        fun initAdapter(bundle: Bundle, fragmentCallback: FragmentCallback): UserListAdapter {
            val adapter = UserListAdapter()
            val list = UserInListConverter().stringToSomeObjectList(bundle.getString("user_list"))
            list?.let { adapter.setList(it) }
            adapter.setCallback(fragmentCallback)
            return adapter
        }
    }

    private fun setCallback(_fragmentCallback: FragmentCallback) {
        fragmentCallback = _fragmentCallback
    }

    fun setList(_userList: List<User>){
        userList = _userList.toMutableList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_view, parent, false),
            fragmentCallback = fragmentCallback
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    fun getLastItemId(): Int? = userList.lastOrNull()?.id

    fun addList(_userList: List<User>) {
        userList.addAll(_userList)
        notifyDataSetChanged()
    }

    fun saveToState(outState: Bundle) {
        outState.putString("user_list", UserInListConverter().someObjectListToString(userList))
    }

    fun updateUser(user: User) {
        userList[userList.indexOfFirst { it.id == user.id }] = user
    }

}

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