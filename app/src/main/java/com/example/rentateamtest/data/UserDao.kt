package com.example.rentateamtest.data

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUsersFromDb() : Observable<List<User>>

    @Insert
    fun insertUser(users: User): Completable

    @Query("DELETE FROM users")
    fun deleteAll(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<User?>): Completable
}