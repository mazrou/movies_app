package com.mazrou.movieApp.ui.main

import android.os.Parcelable
import android.util.Log
import com.mazrou.movieApp.model.Movie
import com.mazrou.movieApp.model.MovieInList
import com.mazrou.movieApp.repository.main.Repository
import com.mazrou.movieApp.ui.BaseViewModel
import com.mazrou.movieApp.ui.main.state.MainStateEvent
import com.mazrou.movieApp.ui.main.state.MainStateEvent.*
import com.mazrou.movieApp.ui.main.state.MainViewState
import com.mazrou.movieApp.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@ExperimentalCoroutinesApi
@FlowPreview
class MainViewModel(
    private val repository: Repository
) : BaseViewModel<MainViewState> (){

    var stateEventGetList : GetMovieList? = null

    override fun handleNewData(data: MainViewState) {
        data.moviesListFields.moviesList?.let {list ->
            insertNewMovieList(list)
        }

        setLastPage(data.moviesListFields.lastPage)

        data.movieDetails?.let {
            setMovieDetails(it)
        }
    }

    private fun setMovieDetails(data : Movie){
        val update = getCurrentViewStateOrNew()
        if (update.movieDetails == data){
            return
        }
        update.movieDetails = data
        setViewState(update)
    }
    private fun insertNewMovieList(data : List<MovieInList>){
        val update = getCurrentViewStateOrNew()
        if (update.moviesListFields.moviesList == data){
            return
        }
        update.moviesListFields.moviesList = data
        setViewState(update)
    }

    private fun setLastPage(lastPage : Int){
        val update = getCurrentViewStateOrNew()
        if (update.moviesListFields.lastPage == lastPage){
            return
        }
        update.moviesListFields.lastPage = lastPage
        setViewState(update)
    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<MainViewState>> = when(stateEvent){
            is GetMovieList -> {
                stateEventGetList = stateEvent
                Log.d(TAG , "MainViewModel : getting the list of movies")
                repository.getMovieList(stateEvent = stateEvent , page = stateEvent.page)
            }
            is GetMovieDetails -> {
                repository.getMovieDetails(stateEvent = stateEvent , id = stateEvent.id)
            }
            else -> {
                flow{
                    emit(
                        DataState.error<MainViewState>(
                            response = Response(
                                message = Constants.INVALID_STATE_EVENT,
                                uiComponentType = UIComponentType.None(),
                                messageType = MessageType.Error()
                            ),
                            stateEvent = stateEvent
                        )
                    )
                }
            }
        }
        launchJob(stateEvent, job)
    }

    override fun initNewViewState()= MainViewState()


    fun setLayoutManagerState(layoutManagerState: Parcelable) {
        val update = getCurrentViewStateOrNew()
        update.moviesListFields.layoutManagerState = layoutManagerState
        setViewState(update)
    }
    fun getPage() = getCurrentViewStateOrNew().moviesListFields.page

    private fun resetPage() {
        val update = getCurrentViewStateOrNew()
        update.moviesListFields.page = 1
        setViewState(update)
    }

    fun loadFirstPage() {
        if (!isJobAlreadyActive(stateEventGetList)) {
            resetPage()
            stateEventGetList = GetMovieList()
            setStateEvent(stateEventGetList!!)
        }
    }

    private fun incrementPageNumber() : Boolean {
        val update = getCurrentViewStateOrNew()
        val page = getPage()
        if (page <= update.moviesListFields.lastPage){
            update.moviesListFields.page = page.plus(1)
            setViewState(update)
            return true
        }
        return false
    }

    fun nextPage() {
        if (!isJobAlreadyActive(stateEventGetList) ) {
            val success = incrementPageNumber()
            if (success){

                stateEventGetList = GetMovieList(page = getPage())
                setStateEvent(stateEventGetList!!)
            }
        }
    }

    fun resetTheMovieDetails() {
        val update = getCurrentViewStateOrNew()
        update.movieDetails = null
        setViewState(update)
    }

}