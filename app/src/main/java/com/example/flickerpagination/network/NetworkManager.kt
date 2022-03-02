package com.example.flickerpagination.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {

    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(getLogging())
            .build()
    }

    private fun getLogging(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.flickr.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var service: GitHubService = retrofit.create(GitHubService::class.java)

}