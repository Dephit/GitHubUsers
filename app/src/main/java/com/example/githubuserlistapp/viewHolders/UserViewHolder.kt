package com.example.githubuserlistapp.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserlistapp.data.User
import com.example.githubuserlistapp.interfaces.FragmentCallback
import com.example.githubuserlistapp.utills.loadUserAvatar
import kotlinx.android.synthetic.main.user_view.view.*

class UserViewHolder(itemView: View, private val fragmentCallback: FragmentCallback) : RecyclerView.ViewHolder(itemView) {

    //bind user and view
    fun bind(user: User) {
        //loads user avatar
        loadUserAvatar(url = user.avatar_url, userAvatar = itemView.user_avatar)
        //sets user login to id_text field
        itemView.id_text.text = user.login
        //sets on item click listener
        itemView.setOnClickListener {
            fragmentCallback.onUserPressed(user.login)
        }
    }
}
