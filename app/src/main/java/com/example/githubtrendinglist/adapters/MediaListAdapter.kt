package com.example.githubtrendinglist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubtrendinglist.adapters.viewHolder.ImageViewHolder
import com.example.githubtrendinglist.adapters.viewHolder.VideoViewHolder
import com.example.githubtrendinglist.databinding.RecyclerMediaImageItemBinding
import com.example.githubtrendinglist.databinding.RecyclerMediaVideoItemBinding
import com.example.githubtrendinglist.model.Datum
import com.example.githubtrendinglist.utils.AppInterface

internal const val VIEW_IMAGE = 0
internal const val VIEW_VIDEO = 1

class MediaListAdapter(private val callback: AppInterface.MediaItemCallback) :
    PagingDataAdapter<Datum, RecyclerView.ViewHolder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Datum>() {
            override fun areItemsTheSame(
                old: Datum,
                new: Datum
            ): Boolean {
                return old.id == new.id
            }

            override fun areContentsTheSame(
                old: Datum,
                new: Datum
            ): Boolean {
                return old == new
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_IMAGE -> {
                ImageViewHolder(
                    RecyclerMediaImageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_VIDEO -> {
                VideoViewHolder(
                    RecyclerMediaVideoItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                throw Exception("No View Found")
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(holder.bindingAdapterPosition)?.let { item ->
            when (getItemViewType(position = holder.bindingAdapterPosition)) {
                VIEW_IMAGE -> {
                    (holder as ImageViewHolder).bind(item).apply {
                        root.setOnClickListener {
                            callback.onItemClick(
                                item,
                                holder.bindingAdapterPosition
                            )
                        }
                    }
                }
                VIEW_VIDEO -> {
                    (holder as VideoViewHolder).bind(item).apply {
                        root.setOnClickListener {
                            callback.onItemClick(
                                item,
                                holder.bindingAdapterPosition
                            )
                        }
                    }
                }
                else -> {
                    throw java.lang.Exception("No view available")
                }
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)?.file_type == 0)
            VIEW_IMAGE
        else
            VIEW_VIDEO
    }


}