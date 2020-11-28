package com.example.githubuserlistapp.utills

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserlistapp.R
import com.squareup.picasso.Picasso

fun loadUserAvatar(url: String, userAvatar: ImageView){
    try {
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(userAvatar)
    }catch (e: Exception){ }
}

//Returns recyclerView's LinearLayoutManager if it's exists
fun RecyclerView.getLinearLayoutManager(): LinearLayoutManager? {
    return if(layoutManager is LinearLayoutManager) {
        layoutManager as LinearLayoutManager
    }else {
        null
    }
}

fun View.setVisible(b: Boolean) {
    visibility = if(b) View.VISIBLE else View.GONE
}