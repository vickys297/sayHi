package com.example.githubtrendinglist.adapters.viewHolder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubtrendinglist.databinding.RecyclerMediaVideoItemBinding
import com.example.githubtrendinglist.model.Datum
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

internal val TAG = VideoViewHolder::class.java.simpleName

class VideoViewHolder(private val binding: RecyclerMediaVideoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var exoPlayer: ExoPlayer
    lateinit var mediaItem: MediaItem

    fun bind(media: Datum): RecyclerMediaVideoItemBinding {

//        mediaItem = MediaItem.fromUri(media.file!!)
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
            .into(binding.viewPlayer)

//        val player = ExoPlayer.Builder(binding.root.context)
//            .build()
//        exoPlayer = player
//        exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
//
//        binding.viewPlayer.apply {
//            hideController()
//            this.player = exoPlayer
//        }

        return binding
    }

    fun stopAndReleasePlayer() {
//        Log.i(TAG, "stopAndReleasePlayer: ")
//        if (exoPlayer.isPlaying) {
//            exoPlayer.stop()
//            exoPlayer.release()
//        }

    }

    fun startPlayer() {
//        Log.i(TAG, "startPlayer: ")
//
//        try {
//            if (!exoPlayer.isPlaying) {
//                exoPlayer.setMediaItem(mediaItem)
//                exoPlayer.prepare()
//                exoPlayer.volume = 0F
//                exoPlayer.play()
//            }
//        } catch (e: Exception) {
//            Log.e(TAG, "startPlayer: ", e)
//        }
    }
}