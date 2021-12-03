package com.mazrou.movieApp.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mazrou.movieApp.model.MovieInList

data class MovieListResponseObject (
    @Expose
    @SerializedName("page")
    val page : Int ,
    @Expose
    @SerializedName("results")
    val results : List<MovieInList> ,
    @Expose
    @SerializedName("total_pages")
    val totalPages : Int ,
    @Expose
    @SerializedName("total_results")
    val totalResults : Int
)