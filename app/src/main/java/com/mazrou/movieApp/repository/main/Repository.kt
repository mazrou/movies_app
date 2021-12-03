package com.mazrou.movieApp.repository.main

import com.mazrou.movieApp.ui.main.state.MainViewState
import com.mazrou.movieApp.util.DataState
import com.mazrou.movieApp.util.StateEvent
import kotlinx.coroutines.flow.Flow


interface Repository {
    fun getMovieList(stateEvent : StateEvent , page : Int) : Flow<DataState<MainViewState>>
    fun getMovieDetails(stateEvent : StateEvent , id : Int) : Flow<DataState<MainViewState>>
}