package com.mazrou.movieApp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.mazrou.movieApp.R
import com.mazrou.movieApp.ui.main.adapter.MovieListAdapter.*
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import com.google.android.flexbox.JustifyContent

import com.google.android.flexbox.FlexDirection

import com.mazrou.movieApp.model.MovieInList
import com.mazrou.movieApp.ui.main.adapter.MovieListAdapter
import com.mazrou.movieApp.ui.main.state.MainStateEvent.*
import com.mazrou.movieApp.util.SafeFlexboxLayoutManager


@FlowPreview
@ExperimentalCoroutinesApi
class MovieListFragment :
    Fragment(R.layout.fragment_movie_list) ,
    Interaction ,
    KodeinAware
{
    override val kodein: Kodein by closestKodein()
    private lateinit var adapter : MovieListAdapter
    private val viewModel  : MainViewModel by instance()
    private var recyclerViewPosition : Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObserver()
        if (savedInstanceState == null){
            getMovies()
        }else{
            recyclerViewPosition = savedInstanceState.getInt("RecyclerViewPosition")
            Log.e("TAGI" , recyclerViewPosition.toString() )
        }
        initView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("RecyclerViewPosition" ,recyclerViewPosition)
        super.onSaveInstanceState(outState)
    }

    private fun subscribeObserver(){
       viewModel.numActiveJobs.observe(viewLifecycleOwner, {
           swipe_refresh_layout.isRefreshing = viewModel.isJobAlreadyActive(viewModel.stateEventGetList)
        })
        viewModel.viewState.observe(viewLifecycleOwner , {
            it.moviesListFields.moviesList?.let { list ->
                adapter.submitList(list)
            }
        })
    }

    private fun initView(){

        adapter = MovieListAdapter(this)
        movie_recycler_view.also {
            val layoutManager = SafeFlexboxLayoutManager(requireContext())
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.SPACE_AROUND
            it.layoutManager = layoutManager //GridLayoutManager(requireContext(), 2)//LinearLayoutManager(requireContext())
            it.adapter = adapter
            it.scrollToPosition(recyclerViewPosition)
            it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    recyclerViewPosition = lastPosition
                    Log.e("TAGI" , recyclerViewPosition.toString() )
                    if (lastPosition == adapter.itemCount.minus(1)) {
                        viewModel.nextPage()
                    }
                }
            })
        }
        swipe_refresh_layout.setOnRefreshListener {
            recyclerViewPosition = 0
            viewModel.loadFirstPage()
        }
    }

    private fun getMovies(){
        viewModel.setStateEvent(
            GetMovieList(viewModel.getPage())
        )
    }

    private fun getMovieById(id : Int) {
        viewModel.setStateEvent(
            GetMovieDetails(id = id)
        )
    }
    override fun onItemSelected(position: Int, item: Any) {
        when(item) {
            is MovieInList -> {
                getMovieById(item.id)
                findNavController().navigate(R.id.action_movieListFragment_to_movieDetailsFragment)
            }
        }
    }
}