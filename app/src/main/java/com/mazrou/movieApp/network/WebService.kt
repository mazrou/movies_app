package com.mazrou.movieApp.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mazrou.movieApp.model.Movie
import com.mazrou.movieApp.model.MovieInList
import com.mazrou.movieApp.network.response.MovieListResponseObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


const val BASE_URL = "https://api.themoviedb.org/3/"

interface WebService {

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("page") page : Int ,
        @Query("api_key") apiKey : String = "c9856d0cb57c3f14bf75bdc6c063b8f3"
    ) : MovieListResponseObject

    @GET("movie/{id}")
    suspend fun getMovieById(
        @Path("id") id : Int,
        @Query("api_key") apiKey : String = "c9856d0cb57c3f14bf75bdc6c063b8f3"
    ) : Movie



    companion object {

        fun invoke(): WebService {

            val gson: Gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val loggingHeader = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.HEADERS

            val httpClient = OkHttpClient.Builder()
                .callTimeout(2, java.util.concurrent.TimeUnit.MINUTES)
                .connectTimeout(1000, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(1000, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(1000, java.util.concurrent.TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(loggingHeader)


            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(WebService::class.java)
        }
    }
}