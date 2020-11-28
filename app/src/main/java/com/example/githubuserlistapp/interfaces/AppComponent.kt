package com.example.githubuserlistapp.interfaces

import com.example.githubuserlistapp.modules.ApiModule
import com.example.githubuserlistapp.activities.MainActivity
import com.example.githubuserlistapp.fragments.UserDetailFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: UserDetailFragment)
}