package com.venu.moviesearchapp.api

import com.google.gson.GsonBuilder
import com.venu.moviesearchapp.model.moviesData.MoviesData
import com.venu.moviesearchapp.utils.Constants
import com.venu.moviesearchapp.utils.Constants.BASE_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/")
     fun getMoviesBySearch(
        @Query("type") type: String?,
        @Query("apikey") apikey: String?,
        @Query("s") movieName: String?,
    ): Call<MoviesData>

    companion object {
        var apiService: ApiService? = null

        fun getInstance(): ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}