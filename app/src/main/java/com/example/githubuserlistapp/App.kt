package com.example.githubuserlistapp

import android.app.Application
import com.example.githubuserlistapp.interfaces.AppComponent
import com.example.githubuserlistapp.interfaces.DaggerAppComponent

class App: Application() {
    val appComponent: AppComponent = DaggerAppComponent.create()
}