package com.example.githubuserlistapp

import android.app.Application

class App: Application() {
    val appComponent: AppComponent = DaggerAppComponent.create()
}