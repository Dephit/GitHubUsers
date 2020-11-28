package com.example.githubuserlistapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule{

    @Singleton
    @Provides
    fun provideApi(): GitHubApi {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApi::class.java)
    }

    @Provides
    fun provideRepository(api: GitHubApi): Repository {
        return RepositoryImpl(api)
    }
}