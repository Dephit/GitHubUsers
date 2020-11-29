package com.example.githubuserlistapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserlistapp.R
import com.example.githubuserlistapp.converters.ListConverter
import com.example.githubuserlistapp.viewHolders.UserViewHolder
import com.example.githubuserlistapp.data.User
import com.example.githubuserlistapp.interfaces.FragmentCallback
import com.google.gson.reflect.TypeToken

class UserListAdapter: RecyclerView.Adapter<UserViewHolder>(){
    private var userList: MutableList<User> = mutableListOf()
    lateinit var fragmentCallback: FragmentCallback

    companion object {
        private const val USER_LIST = "user_list"
        //initializing user list by list
        fun initAdapter(_userList: List<User> = listOf(), fragmentCallback: FragmentCallback): UserListAdapter {
            val adapter = UserListAdapter()
            adapter.setList(_userList)
            adapter.setCallback(fragmentCallback)
            return adapter
        }
        //initializing user list by bundle
        fun initAdapter(bundle: Bundle, fragmentCallback: FragmentCallback): UserListAdapter {
            val adapter = UserListAdapter()
            val list = ListConverter<User>().stringToObjectList(d = bundle.getString(USER_LIST), listType = object : TypeToken<List<User>>() {}.type)
            list?.let { adapter.setList(it) }
            adapter.setCallback(fragmentCallback)
            return adapter
        }
    }

    //sets fragment callback
    private fun setCallback(_fragmentCallback: FragmentCallback) {
        fragmentCallback = _fragmentCallback
    }

    //sets User List
    fun setList(_userList: List<User>){
        userList = _userList.toMutableList()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_view, parent, false),
            fragmentCallback = fragmentCallback
        )
    }

    //bind user object and view holder
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        try {
            holder.bind(userList[position])
        }catch (e: Exception){

        }
    }

    //return user list size
    override fun getItemCount(): Int = userList.size

    //returns last id of user list if it's not null
    fun getLastItemId(): Int? = userList.lastOrNull()?.id

    //updates user list
    fun addList(_userList: List<User>) {
        userList.addAll(_userList)
        notifyDataSetChanged()
    }


    //Saves current list to bundle
    fun saveToState(outState: Bundle) {
        outState.putString(USER_LIST, ListConverter<User>().objectListToString(userList))
    }


    //Checks if user list is empty
    fun isEmpty(): Boolean {
        return itemCount == 0
    }

}

