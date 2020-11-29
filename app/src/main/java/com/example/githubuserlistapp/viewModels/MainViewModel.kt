package com.example.githubuserlistapp.viewModels

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserlistapp.sealedClasses.State
import com.example.githubuserlistapp.adapters.UserListAdapter
import com.example.githubuserlistapp.data.User
import com.example.githubuserlistapp.interfaces.FragmentCallback
import com.example.githubuserlistapp.interfaces.Repository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    private val USER_LOAD_TAG = "USER_LOAD_TAG"
    private val FIRST_VISIBLE_POSITION = "first_visible_position"
    private val STATE = "state"
    private val IS_LIST_REFRESHED = "is_list_refreshed"
    private val SINCE = "since"

    private lateinit var adapter: UserListAdapter
    var position: Int = 0
    var isListRefreshed = false
    val state = MutableLiveData<State>()
    private var since = 0

    //Load user list
    //showLoader is to determine whether we need to show loading state or not
    //_since is to  determine from what user's id we need to load list
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

    //Called after user list loading is completed
    private fun onCompleteUserList() {
        Log.i(USER_LOAD_TAG, "onComplete")
    }
    //Called when we receive an error while loading user list
    private fun onErrorUserList(it: Throwable?) {
        state.postValue(State.ErrorState(it?.message))
        Log.i(USER_LOAD_TAG, it?.message!!)
    }

    //Called when we successively loaded user's list
    private fun onNextUserList(list: List<User>?) {
        //if list is not empty, loaded state is set
        //else if adapter hasn't items NoItemState is set
        list?.let {
            adapter.addList(it)
            isListRefreshed = false
            if(state.value !is State.LoadedState)
                state.postValue(State.LoadedState())
        } ?: if(adapter.isEmpty())
            state.postValue(State.NoItemState)
        Log.i(USER_LOAD_TAG, "onNext")
    }

    //initialization of a viewModel
    fun initialize(fragmentCallback: FragmentCallback, savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            adapter = UserListAdapter.initAdapter(_userList = listOf(), fragmentCallback = fragmentCallback)
            loadUsers()
        }else
            restoreFromInstance(fragmentCallback, savedInstanceState)
    }

    //restoring view model from bundle
    private fun restoreFromInstance(fragmentCallback: FragmentCallback, savedInstanceState: Bundle) {
        with(savedInstanceState){
            adapter = UserListAdapter.initAdapter(bundle = this, fragmentCallback = fragmentCallback)
            position = getInt(FIRST_VISIBLE_POSITION, 0)
            since = getInt(SINCE, 0)
            isListRefreshed = getBoolean(IS_LIST_REFRESHED, false)
            state.postValue(getSerializable(STATE) as State?)
        }
    }

    //Saving state to bundle
    fun saveState(outState: Bundle) {
        with(outState){
            if(state.value !is State.UserOpenState)
                adapter.saveToState(this)
            putSerializable(STATE, state.value)
            putInt(SINCE, since)
            putBoolean(IS_LIST_REFRESHED, isListRefreshed)
            putInt(FIRST_VISIBLE_POSITION, position)
        }
    }

    //returns user adapter
    fun getAdapter(): UserListAdapter = adapter

    //loads user adapter
    fun loadUserProfile(login: String, findFirstVisibleItemPosition: Int?) {
        state.postValue(State.LoadingState)
        position = findFirstVisibleItemPosition!!
        repository.getUser(
            login = login,
            onNext = { onUserProfileLoaded(it) },
            onError = { closeUserProfile() }
        )
    }

    //Called when user profile loaded
    private fun onUserProfileLoaded(user: User) {
        state.postValue(State.UserOpenState(user))
    }

    //Closes user profile
    fun closeUserProfile() {
        state.postValue(State.LoadedState(position))
    }

    //updates current user list
    fun updateList(linearLayoutManager: LinearLayoutManager) {
        with(linearLayoutManager){
            position = findFirstVisibleItemPosition()
            if (childCount + findFirstVisibleItemPosition() >= adapter.itemCount / 2) {
                if (!isListRefreshed){
                    isListRefreshed = true
                    loadUsers(showLoader = false, _since = adapter.getLastItemId())
                }
            }
        }
    }

}
