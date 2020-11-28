package com.example.githubuserlistapp

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: UserDetailFragment)
}