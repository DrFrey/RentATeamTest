package com.example.rentateamtest.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
data class User(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @SerializedName("first_name")
    @ColumnInfo(name = "first_name")
    val firstName: String?,

    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    val lastName: String?,

    @SerializedName("email")
    @ColumnInfo(name = "email")
    val email: String?,

    @SerializedName("avatar")
    @ColumnInfo(name = "avatar")
    val avatar: String?
): Parcelable
