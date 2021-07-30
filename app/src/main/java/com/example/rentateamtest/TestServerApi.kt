package com.example.rentateamtest

import com.example.rentateamtest.data.Response
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://reqres.in/api/"


val interceptor = HttpLoggingInterceptor()
val client =
    OkHttpClient.Builder()
        .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

interface TestServerApi {
    @GET("users")
    fun getUsers() : Observable<Response>
}

object TestService {
    val retrofitService : TestServerApi by lazy {
        retrofit.create(TestServerApi::class.java)
    }
}