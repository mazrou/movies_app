package com.mazrou.movieApp.ui.main.state

import android.os.Parcelable
import com.mazrou.movieApp.model.Movie
import com.mazrou.movieApp.model.MovieInList

class MainViewState(
    var moviesListFields : MovieInListFields = MovieInListFields() ,
    var movieDetails : Movie? = null
)

data class MovieInListFields(
    var moviesList: List<MovieInList>? = null,
    var page: Int = 1,
    var layoutManagerState: Parcelable? = null,
    var lastPage: Int = 1
)