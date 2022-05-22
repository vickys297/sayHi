package com.example.githubtrendinglist.adapters.viewHolder

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubtrendinglist.databinding.RecyclerMediaImageItemBinding
import com.example.githubtrendinglist.model.Datum

class ImageViewHolder(private val binding: RecyclerMediaImageItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(media: Datum): RecyclerMediaImageItemBinding {

        binding.apply {
            executePendingBindings()
        }
        Glide
            .with(binding.root)
            .load(media.file)
            .centerCrop()
            .preload()

        Glide
            .with(binding.root)
            .load(media.file)
            .centerCrop()
            .into(binding.imageView)

        return binding
    }
}