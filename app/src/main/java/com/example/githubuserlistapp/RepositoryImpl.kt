package com.example.githubuserlistapp

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class RepositoryImpl(private val gitHubApi: GitHubApi): Repository {

    override fun getUserList(
        since: String,
        onNext: (List<User>) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ) {
        val observable = gitHubApi.getUserList(since = since)
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onNext(it) },
                { onError(it) },
                { onComplete() }
            )
    }

    override fun getUser(
        login: String,
        onNext: (User) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ) {

    }

    fun getFakeUser(): User{
        return User(
            login = "User's Login",
            location = "Los Angeles"
        )
    }

    fun getFakeUsers(): List<User>{
        val a = mutableListOf<User>()
        for (i in 0..40){
            a.add(User(id = i, login = "User's Login"))
        }
        return a
    }


}