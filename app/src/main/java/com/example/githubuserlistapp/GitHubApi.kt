package com.example.githubuserlistapp

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface GitHubApi{

    @GET(value = "/users")
    @Headers("Authorization: Bearer 5f3dad83d39a2c66f0dc5e75035878ca6564f70a")
    fun getUserList(
        @Query("since")  since: String
    ):Observable<List<User>>
}