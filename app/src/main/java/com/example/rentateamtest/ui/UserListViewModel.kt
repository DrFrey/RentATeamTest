package com.example.rentateamtest.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rentateamtest.data.UserRepository
import com.example.rentateamtest.data.User
import io.reactivex.Observable
import java.lang.IllegalArgumentException

class UserListViewModel(private val repository: UserRepository) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun getUsers(): Observable<List<User>> {
        return repository.getUsers()
            .doOnError {
                Log.d("___", "doOnError")
                _error.postValue(it.message.toString())
            }
            .doOnComplete {
                Log.d("___", "doOnComplete")
                _error.postValue("")
                _isLoading.postValue(false)
            }
            .doOnSubscribe {
                Log.d("___", "doOnSubscribe")
                _isLoading.postValue(true)
            }
    }
}

class UserListViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserListViewModel::class.java)) {
            return UserListViewModel(repository) as T
        }
        throw(IllegalArgumentException("Wrong model class"))
    }
}