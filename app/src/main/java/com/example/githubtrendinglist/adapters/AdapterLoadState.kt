package com.example.githubtrendinglist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubtrendinglist.databinding.RecyclerLoadStateBinding

class AdapterLoadState(
    private val retry: () -> Unit
) : LoadStateAdapter<AdapterLoadState.GitRepositoryLoadStateViewHolder>() {

    class GitRepositoryLoadStateViewHolder(val binding: RecyclerLoadStateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState): RecyclerLoadStateBinding {
            binding.apply {
                executePendingBindings()
            }

            when (loadState) {
                is LoadState.Error -> {
                    Toast.makeText(
                        binding.root.context,
                        loadState.error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.buttonRetry.isVisible = true
                }
                else -> {
                    binding.loading.isVisible = loadState is LoadState.Loading
                    binding.loading.isGone = loadState is LoadState.NotLoading
                    binding.loading.isGone = loadState is LoadState.Error
                    binding.buttonRetry.isVisible = loadState is LoadState.Error
                }
            }
            return binding
        }

    }

    override fun onBindViewHolder(holder: GitRepositoryLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState).apply {
            holder.binding.buttonRetry.setOnClickListener {
                retry.invoke()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): GitRepositoryLoadStateViewHolder {
        return GitRepositoryLoadStateViewHolder(
            RecyclerLoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}