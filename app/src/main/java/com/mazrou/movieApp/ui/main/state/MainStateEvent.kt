package com.mazrou.movieApp.ui.main.state

import com.mazrou.movieApp.util.StateEvent

sealed class MainStateEvent : StateEvent{
    class GetMovieList(
        val page : Int = 1
    ) : MainStateEvent()
    class GetMovieDetails(
        val id : Int
    ) : MainStateEvent()
}
