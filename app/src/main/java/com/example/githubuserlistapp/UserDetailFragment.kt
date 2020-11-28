package com.example.githubuserlistapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import javax.inject.Inject

class UserDetailFragment : Fragment() {

    companion object {
        fun newInstance(login: String): UserDetailFragment{
            val fragment = UserDetailFragment()

            val args = Bundle()
            args.putString("user_login", login)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject lateinit var viewModel: UserDeatailViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_deatail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}