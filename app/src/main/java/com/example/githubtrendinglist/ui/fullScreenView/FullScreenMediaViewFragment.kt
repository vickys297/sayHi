package com.example.githubtrendinglist.ui.fullScreenView

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.githubtrendinglist.R
import com.example.githubtrendinglist.databinding.FragmentFullScreenMediaViewBinding
import com.example.githubtrendinglist.model.Datum
import com.example.githubtrendinglist.utils.AppConstants
import com.example.githubtrendinglist.utils.AppViewModelFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

internal val TAG = FullScreenMediaViewFragment::class.java.simpleName

class FullScreenMediaViewFragment(private val datum: Datum) :
    Fragment(R.layout.fragment_full_screen_media_view) {

    private var player: ExoPlayer? = null
    private lateinit var viewModel: FullScreenMediaViewViewModel
    private lateinit var binding: FragmentFullScreenMediaViewBinding

    companion object {
        fun getInstance(datum: Datum) = FullScreenMediaViewFragment(datum)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFullScreenMediaViewBinding.bind(view)
        setViewModel()


        binding.imageViewBack.setOnClickListener {
            if (player != null && player!!.isPlaying) {
                Log.i(TAG, "onPause: ")
                player!!.stop()
                player!!.release()
                player = null
            }
            findNavController().navigateUp()
        }

        setupView(datum)
    }

    private fun setupView(datum: Datum) {
        when (datum.file_type) {
            AppConstants.FileType.FILE_IMAGE -> {
                Glide
                    .with(binding.root)
                    .load(datum.file)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.imageView)

                binding.imageView.isVisible = true
            }
            AppConstants.FileType.FILE_VIDEO -> {
                player = ExoPlayer.Builder(requireContext()).build()
                val mediaContent = datum.file?.let { MediaItem.fromUri(it) }
                binding.exoPlay.player = player
                if (mediaContent != null) {
                    binding.exoPlay.hideController()
                    player!!.setMediaItem(mediaContent)
                    player!!.prepare()
                    player!!.repeatMode = ExoPlayer.REPEAT_MODE_ALL
                }
                binding.exoPlay.isVisible = true
            }
            else -> {

            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (player != null && !player!!.isPlaying) {
            Log.i(TAG, "onResume: ")
            player!!.play()
        }
    }

    override fun onPause() {
        super.onPause()

        if (player != null && player!!.isPlaying) {
            Log.i(TAG, "onPause: ")
            player!!.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(
            this@FullScreenMediaViewFragment,
            AppViewModelFactory()
        )[FullScreenMediaViewViewModel::class.java]
    }

}