package com.example.githubuserlistapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserlistapp.R
import kotlinx.android.synthetic.main.list_view.*
import javax.inject.Inject

class ListFragment @Inject constructor() : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_view, container, false)
    }

    fun getRecyclerView(): RecyclerView? = recycler_view
}