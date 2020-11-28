package com.example.githubuserlistapp.sealedClasses

import com.example.githubuserlistapp.data.User
import java.io.Serializable

sealed class State: Serializable {
    object LoadingState: State()
    object NoItemState : State()
    class UserOpenState(val user: User): State()
    class ErrorState(val message: String? = ""): State()
    class LoadedState(val lastPosition: Int? = null): State()
}