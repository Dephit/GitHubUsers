package com.example.githubuserlistapp.interfaces

import com.example.githubuserlistapp.data.User
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.*

interface GitHubApi{

    @GET(value = "/users")
    @Headers("Authorization: Bearer 5f3dad83d39a2c66f0dc5e75035878ca6564f70a")
    fun getUserList(
        @Query("since")  since: String
    ):Observable<List<User>>

    @GET(value = "/users/{username}")
    @Headers("Authorization: Bearer 5f3dad83d39a2c66f0dc5e75035878ca6564f70a")
    fun getUser(
            @Path("username")  username: String
    ):Observable<User>
}