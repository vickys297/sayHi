package com.example.githubtrendinglist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubtrendinglist.databinding.RecyclerFileImageListItemBinding
import com.example.githubtrendinglist.databinding.RecyclerFileTitleItemBinding
import com.example.githubtrendinglist.model.GalleryListingModel

internal const val VIEW_TITLE = 0
internal const val VIEW_MEDIA = 1

class FileSelectorAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var galleryListingModel = arrayListOf<GalleryListingModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            VIEW_TITLE -> {
                return TitleViewHolder(
                    RecyclerFileTitleItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_MEDIA -> {
                return MediaViewHolder(
                    RecyclerFileImageListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                throw Exception("New View Attached")
            }
        }

    }

    override fun getItemCount(): Int {
        return galleryListingModel.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (galleryListingModel[position].contentType == VIEW_TITLE) {
            VIEW_TITLE
        } else {
            VIEW_MEDIA
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = galleryListingModel[position]
        when (getItemViewType(position)) {
            VIEW_TITLE -> {
                (holder as TitleViewHolder).bind(item)
            }
            VIEW_MEDIA -> {
                (holder as MediaViewHolder).bind(item)
            }
        }
    }

    class MediaViewHolder(val binding: RecyclerFileImageListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GalleryListingModel) {
            binding.apply {
                executePendingBindings()
            }

            Glide
                .with(binding.root)
                .load(item.data!!.contentUri)
                .centerCrop()
                .into(binding.imageView2)
        }

    }

    class TitleViewHolder(val binding: RecyclerFileTitleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GalleryListingModel) {
            binding.apply {
                dataModel = item
                executePendingBindings()
            }
        }
    }


    fun updateDataset(newDataSet: ArrayList<GalleryListingModel>) {
        val lastItem = galleryListingModel.size - 1
        galleryListingModel = newDataSet
        notifyItemRangeInserted(lastItem, newDataSet.size)
    }
}