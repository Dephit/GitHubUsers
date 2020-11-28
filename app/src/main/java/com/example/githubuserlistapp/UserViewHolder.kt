package com.example.githubuserlistapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_view.view.*

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(userInList: UserInList) {
        try {
            Picasso.get()
                    .load(userInList.avatar_url)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .into(itemView.user_avatar)
        }catch (e: Exception){ }

        itemView.id_text.text = userInList.login
    }
}
