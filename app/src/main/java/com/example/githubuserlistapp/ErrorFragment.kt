package com.example.githubuserlistapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.error_view.*

class ErrorFragment : Fragment() {
    private val PARAM = "param2"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            error_text.text = it.getString(PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.error_view, container, false)
    }

    companion object {

        fun newInstance(param1: String) =
            ErrorFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM, param1)
                }
            }
    }
}

class ListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_view, container, false)

    }

    companion object {
        fun newInstance() = SplashFragment()
    }
}