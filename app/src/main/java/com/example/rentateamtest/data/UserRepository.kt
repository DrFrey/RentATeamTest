package com.example.rentateamtest.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.rentateamtest.TestApplication
import com.example.rentateamtest.TestService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class UserRepository(private val userDao: UserDao) {


    fun getUsers(): Observable<List<User>> {
        return Observable.concatArrayEager(
            userDao.getUsersFromDb().subscribeOn(Schedulers.io()),
            Observable.defer {
                if (isInternetAvailable()) {
                    TestService.retrofitService.getUsers()
                        .subscribeOn(Schedulers.io())
                        .map {
                            res -> res.data
                        }
                        .flatMap { l ->
                            userDao.deleteAll()
                                .andThen(userDao.insertAll(l))
                                .toObservable<List<User>>()
                        }
                } else
                    Observable.empty()
            }.subscribeOn(Schedulers.io())
        )
    }

    private fun isInternetAvailable(): Boolean {
        var result = false
        val context = TestApplication.instance.applicationContext
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }
}