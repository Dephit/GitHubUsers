package com.example.githubuserlistapp.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import com.example.githubuserlistapp.R
import com.example.githubuserlistapp.data.User
import com.example.githubuserlistapp.utills.loadUserAvatar
import kotlinx.android.synthetic.main.user_deatail_fragment.*

class UserDetailFragment : Fragment() {

    companion object {
        fun newInstance(user: User): UserDetailFragment {
            val fragment = UserDetailFragment()
            val args = Bundle()
            args.putSerializable("user", user)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_deatail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        setUserView((arguments?.getSerializable("user") as User))
    }

    private fun setUserView(user: User) {
        loadUserAvatar(url = user.avatar_url, userAvatar = user_avatar)
        user_link.text = user.html_url
        user_login.text = user.login
        user_location.text = user.location

        user_link.setOnClickListener {
            context?.apply {
                val builder = CustomTabsIntent.Builder()
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    builder.setToolbarColor(getColor(R.color.purple_500))
                }else {
                    resources.getColor(R.color.purple_500)
                }
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(this, Uri.parse(user.html_url))
            }
        }
    }

}