package com.mazrou.movieApp.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mazrou.movieApp.model.MovieInList

const val PAGINATION_PAGE_SIZE = 20
@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieInList(movieInList: MovieInList): Long

    @Query("SELECT * FROM movie_in_list ORDER BY page , orderInPage ASC LIMIT (:page * :pageSize)")
    fun getMovieInList(
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): List<MovieInList>

}