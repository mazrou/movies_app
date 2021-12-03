package com.mazrou.movieApp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Movie (
     @Expose
     @SerializedName("id")
     val id: Int,
     @Expose
     @SerializedName("title")
     val title : String,
     @Expose
     @SerializedName("overview")
     val overview : String,
     @Expose
     @SerializedName("tagline")
     val tagline : String,
     @Expose
     @SerializedName("original_title")
     val originalTitle : String,
     @Expose
     @SerializedName("backdrop_path")
     val backdropPath : String,
     @Expose
     @SerializedName("release_date")
     val releaseDate : String,
     @Expose
     @SerializedName("production_companies")
     val productionCompanies : List<ProductionCompany>,
     @Expose
     @SerializedName("vote_average")
     val voteAverage : Double,
     @Expose
     @SerializedName("poster_path")
     val posterPath : String,


)

data class ProductionCompany(
    @Expose
    @SerializedName("logo_path")
    val logoPath : String,
    @Expose
    @SerializedName("name")
    val name : String,
    @Expose
    @SerializedName("id")
    val id: Int,
)