package com.example.githubuserlistapp

import io.reactivex.rxjava3.core.Observable

interface Repository {

    fun getUserList(since: String, onNext: (List<User>) -> Unit = {}, onError: (Throwable) -> Unit = {}, onComplete: ()-> Unit = {})
    fun getUser(login: String, onNext: (User) -> Unit = {}, onError: (Throwable) -> Unit = {}, onComplete: ()-> Unit = {})

}
