package com.example.githubuserlistapp

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.Serializable
import javax.inject.Inject

sealed class State: Serializable{
    object LoadingState: State()
    class ErrorState(val message: String? = ""): State()
    class LoadedState(val userList: List<UserInList>): State()
}

class LoginViewModel @Inject constructor(private val api: GitHubApi): ViewModel() {
    private val USER_LOAD_TAG = "USER_LOAD_TAG"

    private lateinit var adapter: UserListAdapter
    var position: Int = 0
    var isListRefreshed = false
    val state = MutableLiveData<State>()
    private var since = 0

    fun loadFakeUsers(showLoader: Boolean = true, _since: Int? = since) {
        since = _since ?: 0
        if(showLoader)
            state.postValue(State.LoadingState)
        val a = mutableListOf<UserInList>()
        for (i in 0..40){
            a.add(UserInList(id = i))
        }
        val observable = Observable.just(a)
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { onNextUserList(it) },
                        { onErrorUserList(it) },
                        { onCompleteUserList() }
                )
    }

    fun loadUsers(showLoader: Boolean = true, _since: Int? = since) {
        Log.i(USER_LOAD_TAG, "load users called")
        since = _since ?: 0
        if(showLoader)
            state.postValue(State.LoadingState)
        val observable = api.getUserList(since = since.toString())
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onNextUserList(it) },
                { onErrorUserList(it) },
                { onCompleteUserList() }
            )
    }

    private fun onCompleteUserList() {
        Log.i(USER_LOAD_TAG, "onComplete")
    }

    private fun onErrorUserList(it: Throwable?) {
        state.postValue(State.ErrorState(it?.message))
        Log.i(USER_LOAD_TAG, it?.message!!)
    }

    private fun onNextUserList(list: List<UserInList>?) {
        list?.let {
            adapter.addList(it)
            isListRefreshed = false
            if(state.value !is State.LoadedState)
                state.postValue(State.LoadedState(it))
        }
        Log.i(USER_LOAD_TAG, "onNext")
    }

    fun initialize(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            adapter = UserListAdapter.initAdapter(listOf())
            loadUsers()
        }else
            restoreFromInstance(savedInstanceState)
    }

    private fun restoreFromInstance(savedInstanceState: Bundle) {
        with(savedInstanceState){
            adapter = UserListAdapter.initAdapter(this)
            position = getInt("first_visible_position")
            since = getInt("since")
            isListRefreshed = getBoolean("is_list_refreshed")
            state.postValue(getSerializable("state") as State?)
            onNextUserList(listOf())
        }
    }

    fun saveState(outState: Bundle) {
        with(outState){
            adapter.saveToState(this)
            putSerializable("state", state.value)
            putInt("since", since)
            putBoolean("is_list_refreshed", isListRefreshed)
        }
    }

    fun getAdapter(): UserListAdapter = adapter

}
