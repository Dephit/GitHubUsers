package com.example.githubuserlistapp

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.Serializable

import javax.inject.Inject

sealed class State: Serializable{
    object LoadingState: State()
    object UserOpenState: State()
    class ErrorState(val message: String? = ""): State()
    class LoadedState(val lastPosition: Int? = null): State()
}

class LoginViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private val USER_LOAD_TAG = "USER_LOAD_TAG"

    private lateinit var adapter: UserListAdapter
    var position: Int = 0
    var isListRefreshed = false
    val state = MutableLiveData<State>()
    private var since = 0

    fun loadUsers(showLoader: Boolean = true, _since: Int? = since) {
        Log.i(USER_LOAD_TAG, "load users called")
        since = _since ?: 0
        if(showLoader)
            state.postValue(State.LoadingState)
        repository.getUserList(
            since = since.toString(),
            onNext = { onNextUserList(it) },
            onError = { onErrorUserList(it)},
            onComplete = { onCompleteUserList() }
        )
    }

    private fun onCompleteUserList() {
        Log.i(USER_LOAD_TAG, "onComplete")
    }

    private fun onErrorUserList(it: Throwable?) {
        state.postValue(State.ErrorState(it?.message))
        Log.i(USER_LOAD_TAG, it?.message!!)
    }

    private fun onNextUserList(list: List<User>?) {
        list?.let {
            adapter.addList(it)
            isListRefreshed = false
            if(state.value !is State.LoadedState)
                state.postValue(State.LoadedState())
        }
        Log.i(USER_LOAD_TAG, "onNext")
    }

    fun initialize(fragmentCallback: FragmentCallback, savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            adapter = UserListAdapter.initAdapter(
                _userList = listOf(),
                fragmentCallback = fragmentCallback
            )
            loadUsers()
        }else
            restoreFromInstance(fragmentCallback, savedInstanceState)
    }

    private fun restoreFromInstance(fragmentCallback: FragmentCallback, savedInstanceState: Bundle) {
        with(savedInstanceState){
            adapter = UserListAdapter.initAdapter(
                bundle = this,
                fragmentCallback = fragmentCallback
            )
            position = getInt("first_visible_position")
            since = getInt("since")
            isListRefreshed = getBoolean("is_list_refreshed")
            state.postValue(getSerializable("state") as State?)
            Log.i("RESTORED_STATE", (getSerializable("state") as State?).toString())
        }
    }

    fun saveState(outState: Bundle) {
        with(outState){
            adapter.saveToState(this)
            putSerializable("state", state.value)
            putInt("since", since)
            putBoolean("is_list_refreshed", isListRefreshed)
            putInt("first_visible_position", position)
        }
    }

    fun getAdapter(): UserListAdapter = adapter

    fun setUserProfile(login: String, findFirstVisibleItemPosition: Int?) {
        state.postValue(State.LoadingState)
        position = findFirstVisibleItemPosition!!
        repository.getUser(
            login = login,
            onNext = { changeStateToOpen(it) },
            onError = { closeUserProfile() }
        )

    }

    private fun changeStateToOpen(user: User) {
        adapter.updateUser(user)
        state.postValue(State.UserOpenState)
    }

    fun closeUserProfile() {
        state.postValue(State.LoadedState(position))
    }

}
