package com.example.githubtrendinglist.ui.fullScreenView

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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

    private var videoPlayer: ExoPlayer? = null
    private lateinit var viewModel: FullScreenMediaViewViewModel
    private lateinit var binding: FragmentFullScreenMediaViewBinding

    companion object {
        fun getInstance(datum: Datum) = FullScreenMediaViewFragment(datum)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFullScreenMediaViewBinding.bind(view)
        setViewModel()

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
                    .into(binding.imageViewPreview)

                binding.exoPlay.isVisible = false
                binding.imageViewPreview.isVisible = true
            }

            AppConstants.FileType.FILE_VIDEO -> {
                videoPlayer = ExoPlayer.Builder(requireContext()).build()
                binding.exoPlay.player = videoPlayer
                binding.exoPlay.hideController()
                val mediaItem: MediaItem = MediaItem.fromUri(datum.file!!)
                videoPlayer?.let {
                    it.setMediaItem(mediaItem)
                    it.prepare()
                }
                binding.exoPlay.isVisible = true
                binding.imageViewPreview.isVisible = false
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (videoPlayer != null && !videoPlayer!!.isPlaying) {
            videoPlayer!!.play()
        }
    }

    override fun onPause() {
        super.onPause()
        if (videoPlayer != null && videoPlayer!!.isPlaying) {
            videoPlayer!!.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer = null
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(
            this@FullScreenMediaViewFragment,
            AppViewModelFactory()
        )[FullScreenMediaViewViewModel::class.java]
    }

}