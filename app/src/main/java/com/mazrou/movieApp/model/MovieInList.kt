package com.mazrou.movieApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*

"original_language": "en",
"original_title": "Venom: Let There Be Carnage",
"overview": "After finding a host body in investigative reporter Eddie Brock, the alien symbiote must face a new enemy, Carnage, the alter ego of serial killer Cletus Kasady.",
"popularity": 13071.277


"video": false,
 */

@Entity(tableName = "movie_in_list")
data class MovieInList (
    @PrimaryKey(autoGenerate = false)
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("title")
    val title : String,
   /* @Expose
    @SerializedName("overview")
    val overview : String,
    @Expose
    @SerializedName("backdrop_path")
    val backdropPath : String, */
    @Expose
    @SerializedName("poster_path")
    val posterPath : String,

    // we use those attribute to keep the order of the movies
    // because the IDs are not ordered .
    var page : Int,
    var orderInPage : Int
 /*   @Expose
    @SerializedName("release_date")
    val releaseDate : String,
    @Expose
    @SerializedName("adult")
    val adult : Boolean,
    @Expose
    @SerializedName("vote_average")
    val voteAverage : Double,
    @Expose
    @SerializedName("vote_count")
    val voteCount : Long */
)