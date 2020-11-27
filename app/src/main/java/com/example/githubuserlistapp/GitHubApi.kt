package com.example.githubuserlistapp

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi{

    @GET(value = "/users")
    fun getUserList(
        @Query("since")  since: String
    ):Observable<List<UserInList>>
}