package com.example.githubuserlistapp.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserlistapp.data.User
import com.example.githubuserlistapp.interfaces.FragmentCallback
import com.example.githubuserlistapp.utills.loadUserAvatar
import kotlinx.android.synthetic.main.user_view.view.*

class UserViewHolder(itemView: View, private val fragmentCallback: FragmentCallback) : RecyclerView.ViewHolder(itemView) {

    fun bind(user: User) {
        loadUserAvatar(url = user.avatar_url, userAvatar = itemView.user_avatar)

        itemView.id_text.text = user.login
        itemView.setOnClickListener {
            fragmentCallback.onUserPressed(user.login)
        }
    }
}
