package com.mtanmay.imagegallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mtanmay.imagegallery.databinding.LoadStateBinding

class LoadstateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadstateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = LoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class ViewHolder(
        private val binding: LoadStateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                binding.loading.isVisible = loadState is LoadState.Loading
                binding.noConnection.isVisible = loadState !is LoadState.Loading
                binding.btnRetry.isVisible = loadState !is LoadState.Loading
            }
        }

    }
}