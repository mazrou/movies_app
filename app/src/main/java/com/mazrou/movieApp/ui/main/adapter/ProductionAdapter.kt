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
import com.mazrou.movieApp.model.ProductionCompany
import kotlinx.android.synthetic.main.production__item.view.*


class ProductionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductionCompany>() {

        override fun areItemsTheSame(oldItem: ProductionCompany, newItem: ProductionCompany): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductionCompany, newItem: ProductionCompany): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.production__item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<ProductionCompany>) {
        differ.submitList(list)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                holder.bind(differ.currentList[position])

            }
        }
    }


    inner class ImageViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ProductionCompany) = with(itemView) {

            name_txt_view.text = item.name

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f

            circularProgressDrawable.start()

            Glide
                .with(this)
                .load("https://image.tmdb.org/t/p/w200/${item.logoPath}")
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(image_view)
        }
    }
}