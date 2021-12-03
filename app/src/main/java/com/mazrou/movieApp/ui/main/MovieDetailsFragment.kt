package com.mazrou.movieApp.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.mazrou.movieApp.R
import com.mazrou.movieApp.ui.main.adapter.ProductionAdapter
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

@FlowPreview
@ExperimentalCoroutinesApi
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) , KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModel  : MainViewModel by instance()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObserver()
    }

    override fun onDestroyView() {
        viewModel.resetTheMovieDetails()
        super.onDestroyView()
    }

    private fun subscribeObserver(){
        viewModel.viewState.observe(viewLifecycleOwner , {
            it.movieDetails?.let { movie ->
                title_txt_view.text = movie.title
                overview_txt_view.text = movie.title
                rating_txt_view.text = movie.voteAverage.toString()
                date_release_txt_view.text = movie.releaseDate
                original_title_txt_view.text = movie.originalTitle
                tagline_txt_view.text = movie.tagline
                val circularProgressDrawable = CircularProgressDrawable(requireContext())
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f

                circularProgressDrawable.start()

                Glide
                    .with(this)
                    .load("https://image.tmdb.org/t/p/w500/${movie.backdropPath}")
                    .centerCrop()
                    .placeholder(circularProgressDrawable)
                    .into(poster_img_view)

                Glide
                    .with(this)
                    .load("https://image.tmdb.org/t/p/w500/${movie.posterPath}")
                    .centerCrop()
                    .placeholder(circularProgressDrawable)
                    .into(image_view)

            }
        })
    }
}