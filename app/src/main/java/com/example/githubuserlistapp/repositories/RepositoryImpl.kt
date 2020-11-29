package com.example.githubuserlistapp.repositories

import android.util.Log
import com.example.githubuserlistapp.data.User
import com.example.githubuserlistapp.interfaces.GitHubApi
import com.example.githubuserlistapp.interfaces.Repository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class RepositoryImpl(private val gitHubApi: GitHubApi): Repository {

    private fun<T> callOn(observable: Observable<T>,
                          onNext: (T) -> Unit,
                          onError: (Throwable) -> Unit,
                          onComplete: () -> Unit
    ){
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onNext(it) },
                        { onError(it) },
                        { onComplete() }
                )
    }

    private fun getFakeUser(): User {
        return User(
            login = "User's Login",
            location = "Los Angeles"
        )
    }

    private fun getFakeUsers(): List<User>{
        val a = mutableListOf<User>()
        for (i in 0..40){
            a.add(User(id = i, login = "User's Login"))
        }
        return a
    }

    override fun getUserList(
        since: String,
        onNext: (List<User>) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ) {
        callOn(gitHubApi.getUserList(since = since), onNext, onError, onComplete)
    }

    override fun getUser(
        login: String,
        onNext: (User) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ) {
        callOn(gitHubApi.getUser(login), onNext, onError, onComplete)
    }
}