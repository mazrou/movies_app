package com.mazrou.movieApp.repository.main

import android.util.Log
import com.mazrou.movieApp.model.Movie
import com.mazrou.movieApp.model.MovieInList
import com.mazrou.movieApp.network.WebService
import com.mazrou.movieApp.network.response.MovieListResponseObject
import com.mazrou.movieApp.persistance.MoviesDao
import com.mazrou.movieApp.repository.NetworkBoundResource
import com.mazrou.movieApp.repository.safeApiCall
import com.mazrou.movieApp.ui.main.state.MainViewState
import com.mazrou.movieApp.ui.main.state.MovieInListFields
import com.mazrou.movieApp.util.ApiResponseHandler
import com.mazrou.movieApp.util.DataState
import com.mazrou.movieApp.util.StateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@FlowPreview
class RepositoryImpl (
    val webService: WebService ,
    val dao : MoviesDao
    ): Repository {


    private var lastPage : Int = 1
    private val TAG = "AppDebug"
    override fun getMovieList(stateEvent : StateEvent , page : Int): Flow<DataState<MainViewState>>  {
        Log.d(TAG , "RepositoryImpl : starting the getMovieList function")
        return object : NetworkBoundResource<MovieListResponseObject, List<MovieInList> , MainViewState>(
            apiCall = {webService.getMovies(page = page)},
            cacheCall = {dao.getMovieInList(page = page)},
            dispatcher = IO,
            stateEvent = stateEvent
        ){
            override suspend fun updateCache(networkObject: MovieListResponseObject) {
                Log.d(TAG , "RepositoryImpl : updating the cache by list of movies")
                CoroutineScope(IO).launch {
                    lastPage = networkObject.totalPages
                    Log.d(TAG , "The last page is : $lastPage")
                    networkObject.results.forEachIndexed { index, movieInList ->
                         launch {   // I make this to insert the items in parallel
                             try {
                                 movieInList.page = page
                                 movieInList.orderInPage = index
                                 dao.insertMovieInList(movieInList)
                             }catch (e : Exception){
                                 Log.e(TAG , "${e.message}")
                             }
                         }
                     }
                }
            }

            override fun handleCacheSuccess(resultObj: List<MovieInList>): DataState<MainViewState> {
                    return DataState.data(
                        data = MainViewState(
                            MovieInListFields(
                                moviesList = resultObj ,
                                page = page ,
                                lastPage = lastPage
                            )
                        ),
                        response = null, // we just get the data , so we don't need to display something
                        stateEvent = stateEvent
                    )
            }
        }.result
    }

    override fun getMovieDetails(stateEvent : StateEvent , id : Int): Flow<DataState<MainViewState>> = flow{

        val apiCall = safeApiCall(IO){webService.getMovieById(id = id)}

        emit(
            object : ApiResponseHandler<MainViewState , Movie>(
                response = apiCall ,
                stateEvent = stateEvent
            ){
                override suspend fun handleSuccess(resultObj: Movie): DataState<MainViewState> {
                    return DataState.data(
                        response = null ,
                        data = MainViewState(
                            movieDetails = resultObj
                        ),
                        stateEvent = stateEvent
                    )
                }

            }.getResult()
        )
    }
}