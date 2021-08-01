package com.example.rentateamtest.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rentateamtest.data.UserRepository
import com.example.rentateamtest.data.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalArgumentException

class UserListViewModel(private val repository: UserRepository) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private val disposable = CompositeDisposable()

    init {
        getUsers()
    }

    private fun getUsers() {
        disposable.add(
            repository.getUsers()
                .doOnSubscribe {
                    Log.d("___", "doOnSubscribe")
                    _isLoading.postValue(true)
                }.doFinally {
                    Log.d("___", "doFinally")
                    _isLoading.postValue(false)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("___", "subscribe success")
                    _users.postValue(it)
                    _isLoading.postValue(false)
                    if (repository.error.isNotEmpty()) {
                        _error.postValue(repository.error)
                        repository.error = ""
                    }
                }, {
                    Log.d("___", "subscribe error")
                    Log.d("___", it?.message.toString())
                    _isLoading.postValue(false)
                })
        )
    }

    override fun onCleared() {
        disposable.clear()
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