package com.mtanmay.imagegallery.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mtanmay.imagegallery.api.Result
import com.mtanmay.imagegallery.databinding.ImageItemBinding

class GalleryAdapter() : PagingDataAdapter<Result, GalleryAdapter.ViewHolder>(DIFF_UTIL_CALLBACK) {

    companion object {
        private val DIFF_UTIL_CALLBACK = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null)
            holder.bind(currentItem)
    }

    class ViewHolder(
        private val binding: ImageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {

            binding.apply {
                Glide.with(itemView)
                    .load(result.url_s)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(image)
            }
        }
    }

}