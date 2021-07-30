package com.example.rentateamtest

import android.app.Application
import com.example.rentateamtest.data.UserDatabase
import com.example.rentateamtest.data.UserRepository

class TestApplication : Application() {
    companion object {
        lateinit var instance: Application

        private val database by lazy {
            UserDatabase.getDatabase(instance)
        }

        val repository by lazy {
            UserRepository(database.userDao())
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}