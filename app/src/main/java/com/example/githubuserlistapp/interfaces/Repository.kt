package com.example.githubuserlistapp.interfaces

import com.example.githubuserlistapp.data.User

interface Repository {
    fun getUserList(since: String, onNext: (List<User>) -> Unit = {}, onError: (Throwable) -> Unit = {}, onComplete: ()-> Unit = {})
    fun getUser(login: String, onNext: (User) -> Unit = {}, onError: (Throwable) -> Unit = {}, onComplete: ()-> Unit = {})
}
