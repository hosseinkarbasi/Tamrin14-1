package com.example.flickerpagination.network

import com.example.flickerpagination.data.FlickerResult
import retrofit2.Call
import retrofit2.http.*

interface GitHubService {

    @POST("services/rest")
    fun getImage(
        @Query("api_key") apiKey: String,
        @Query("method") method: String,
        @Query("user_id") user_id: String,
        @Query("extras") extras: String,
        @Query("format") format: String,
        @Query("nojsoncallback") nojsoncallback: String,
        @Query("per_page") per_page: String,
        @Query("page") page: String,

        ): Call<FlickerResult>
}