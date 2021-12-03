package com.mazrou.movieApp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.mazrou.movieApp.R
import com.mazrou.movieApp.model.MovieInList
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieListAdapter(
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieInList>() {

        override fun areItemsTheSame(oldItem: MovieInList, newItem: MovieInList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieInList, newItem: MovieInList): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MovieItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_list_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<MovieInList>) {
        differ.submitList(list)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieItemViewHolder -> {
                holder.bind(differ.currentList[position])

            }
        }
    }


    inner class MovieItemViewHolder constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView)  {

        fun bind( item : MovieInList) = with(itemView){

            title_txt_view.text = item.title

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f

            circularProgressDrawable.start()

            Glide
                .with(this)
                .load("https://image.tmdb.org/t/p/w300/${item.posterPath}")
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(image_view)


            // on click the root layout
            // in this case we pass to the details fragment
            root.setOnClickListener {
               interaction?.onItemSelected(adapterPosition , item)
           }
        }



    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Any)
    }
}