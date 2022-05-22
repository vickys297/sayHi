package com.example.githubtrendinglist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubtrendinglist.databinding.RecyclerFileMediaListItemBinding
import com.example.githubtrendinglist.model.GalleryModel
import com.example.githubtrendinglist.utils.AppInterface


class FileSelectorAdapter(val galleyItemCallback: AppInterface.GalleryOnItemSelect) :
    PagingDataAdapter<GalleryModel, RecyclerView.ViewHolder>(DiffUtil) {

    companion object {
        val DiffUtil = object : DiffUtil.ItemCallback<GalleryModel>() {
            override fun areItemsTheSame(
                oldItem: GalleryModel,
                newItem: GalleryModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: GalleryModel,
                newItem: GalleryModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MediaViewHolder(
            RecyclerFileMediaListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(holder.bindingAdapterPosition)
        item?.run {
            (holder as MediaViewHolder).apply {
                bind(this@run, galleyItemCallback)

            }
        }
    }

    class MediaViewHolder(val binding: RecyclerFileMediaListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GalleryModel, galleyItemCallback: AppInterface.GalleryOnItemSelect) {

            binding.apply {
                dataModel = item
                executePendingBindings()
            }

            binding.imageViewVideoIcon.isVisible = item.mimeType == "video/mp4"

            Glide
                .with(binding.root)
                .load(item.contentUri)
                .centerCrop()
                .into(binding.imageView2)

            binding.root.setOnClickListener {
                galleyItemCallback.onItemClick(item)
            }
        }
    }
}