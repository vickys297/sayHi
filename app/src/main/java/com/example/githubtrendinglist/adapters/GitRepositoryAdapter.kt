package com.example.githubtrendinglist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubtrendinglist.databinding.RecyclerGitRepoLlistItemBinding
import com.example.githubtrendinglist.model.GitRepositoryItem

class GitRepositoryAdapter :
    PagingDataAdapter<GitRepositoryItem, GitRepositoryAdapter.GitRepoViewHolder>(DIFF) {


    class GitRepoViewHolder(private val binding: RecyclerGitRepoLlistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GitRepositoryItem): RecyclerGitRepoLlistItemBinding {
            return binding.apply {
                dataModel = item
                executePendingBindings()
            }
        }

    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<GitRepositoryItem>() {
            override fun areItemsTheSame(
                oldGitRepositoryItem: GitRepositoryItem,
                newGitRepositoryItem: GitRepositoryItem
            ): Boolean {
                return oldGitRepositoryItem == newGitRepositoryItem
            }

            override fun areContentsTheSame(
                oldGitRepositoryItem: GitRepositoryItem,
                newGitRepositoryItem: GitRepositoryItem
            ): Boolean {
                return oldGitRepositoryItem.id == newGitRepositoryItem.id
            }

        }
    }

    override fun onBindViewHolder(holder: GitRepoViewHolder, position: Int) {
        val item = getItem(position)
        item?.run {
            holder.bind(this@run).let {
                Glide
                    .with(it.root)
                    .load(this.owner.avatarURL)
                    .centerCrop()
                    .circleCrop()
                    .into(it.imageViewAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitRepoViewHolder {
        return GitRepoViewHolder(
            RecyclerGitRepoLlistItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}