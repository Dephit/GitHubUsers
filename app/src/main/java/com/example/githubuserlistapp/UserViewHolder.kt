package com.example.githubuserlistapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.user_view.view.*

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(userInList: UserInList) {
        itemView.id_text.text = userInList.id.toString()
    }
}
