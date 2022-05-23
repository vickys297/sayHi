package com.example.githubtrendinglist.ui.uploadVideo

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.abedelazizshe.lightcompressorlibrary.config.Configuration
import com.bumptech.glide.Glide
import com.example.githubtrendinglist.R
import com.example.githubtrendinglist.databinding.FragmentUploadVideoBinding
import com.example.githubtrendinglist.model.FileUploadResponse
import com.example.githubtrendinglist.model.GalleryModel
import com.example.githubtrendinglist.utils.AppConstants
import com.example.githubtrendinglist.utils.AppFunctions
import com.example.githubtrendinglist.utils.AppViewModelFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.material.snackbar.Snackbar
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


internal val TAG = UploadVideoFragment::class.java.simpleName

class UploadVideoFragment : Fragment(R.layout.fragment_upload_video) {

    private var videoPlayer: ExoPlayer? = null
    private lateinit var viewModel: UploadVideoViewModel
    private lateinit var binding: FragmentUploadVideoBinding

    lateinit var videoFileCompressionThread: Thread

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUploadVideoBinding.bind(view)
        viewModel = ViewModelProvider(this, AppViewModelFactory())[UploadVideoViewModel::class.java]


        requireArguments().getParcelable<GalleryModel>(AppConstants.NavigationKey.FILE_KEY)
            ?.let { it ->
                binding.textViewFileName.text = it.name
                if (it.mimeType == "video/mp4") {
                    showVideo(it)
                } else {
                    showImage(it)
                }
            }

        binding.imageButton.setOnClickListener {
            if (videoPlayer != null) {
                videoPlayer!!.stop()
                videoPlayer!!.release()
            }
            findNavController().navigateUp()
        }


    }


    private fun showImage(it: GalleryModel) {

        val originalFile = File(it.fileData)
        val destinationFile =
            File(requireActivity().filesDir.path + "/image/" + it.name)

        originalFile.copyTo(
            destinationFile,
            true
        )
        viewLifecycleOwner.lifecycleScope.launch {
            val compressedImageFile =
                Compressor.compress(requireContext(), destinationFile)

            Glide
                .with(binding.root)
                .load(destinationFile)
                .centerCrop()
                .into(binding.imageViewPreview)

            binding.imageViewPreview.isVisible = true

            binding.buttonUpload.setOnClickListener {
                val requestBody =
                    compressedImageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val fileToUpload = createFormData(
                    "fileToUpload",
                    compressedImageFile.name,
                    requestBody
                )
                val fileType =
                    "0".toRequestBody("multipart/form-data".toMediaTypeOrNull())

                uploadFile(fileToUpload, fileType, destinationFile)

                binding.buttonUpload.apply {
                    text = getString(R.string.Uploading)
                    isEnabled = false
                }
            }
        }


    }


    private fun showVideo(it: GalleryModel) {

        val originalFile = File(it.fileData)
        val destinationFile =
            File(requireActivity().filesDir.path + "/image/" + it.name)


        originalFile.copyTo(
            destinationFile,
            true
        )

        videoPlayer = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = videoPlayer

        lifecycleScope.launch(Dispatchers.IO) {
            VideoCompressor.start(
                context = requireContext().applicationContext, // => This is required
                uris = listOf(Uri.parse(it.contentUri)), // => Source can be provided as content uris
                isStreamable = true,
                saveAt = Environment.DIRECTORY_MOVIES, // => the directory to save the compressed video(s)
                listener = object : CompressionListener {
                    override fun onProgress(index: Int, percent: Float) {
                        Log.i(TAG, "onProgress: $percent")

                        requireActivity().runOnUiThread {
                            binding.progressBar.progress = percent.toInt()
                        }
                    }

                    override fun onStart(index: Int) {
                        binding.progressBar.isVisible = true
                        binding.playerView.isEnabled = false
                        binding.buttonUpload.isEnabled = false
                    }

                    override fun onSuccess(index: Int, size: Long, path: String?) {
                        binding.progressBar.isVisible = false
                        binding.playerView.isEnabled = true
                        binding.buttonUpload.isEnabled = true

                        val mediaItem: MediaItem = MediaItem.fromUri(path!!)
                        videoPlayer?.let {
                            it.setMediaItem(mediaItem)
                            it.prepare()
                            it.play()
                        }

                        binding.buttonUpload.setOnClickListener {
                            val requestBody =
                                File(path).asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val fileToUpload = createFormData(
                                "fileToUpload",
                                File(path).name,
                                requestBody
                            )
                            val fileType =
                                "1".toRequestBody("multipart/form-data".toMediaTypeOrNull())

                            uploadFile(fileToUpload, fileType, destinationFile)

                            binding.buttonUpload.apply {
                                text = getString(R.string.Uploading)
                                isEnabled = false
                            }
                        }

                    }

                    override fun onFailure(index: Int, failureMessage: String) {
                        Log.i(TAG, "onProgress: $failureMessage")
                    }

                    override fun onCancelled(index: Int) {
                        Log.i(TAG, "onProgress: $index")
                    }

                },
                configureWith = Configuration(
                    quality = VideoQuality.HIGH,
                    frameRate = 30, /*Int, ignore, or null*/
                    isMinBitrateCheckEnabled = false,
                    disableAudio = true, /*Boolean, or ignore*/
                    keepOriginalResolution = true, /*Boolean, or ignore*/

                )
            )
        }
        binding.playerView.isVisible = true
    }


    private fun uploadFile(
        fileToUpload: MultipartBody.Part,
        fileType: RequestBody,
        destinationFile: File
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            val response = viewModel.uploadFile(fileToUpload, fileType)

            Log.i(TAG, "uploadFile: $response")
            when (response) {
                is FileUploadResponse.Success -> {

                    if (videoPlayer != null) {
                        videoPlayer!!.stop()
                        videoPlayer!!.release()
                    }

                    destinationFile.delete()

                    AppFunctions.showAlertDialog(
                        context = requireContext(),
                        message = "File Uploaded successfully",
                        positiveButtonName = "Ok",
                        onPositiveClick = { dialog, p1 ->
                            dialog.dismiss()
                            findNavController().navigateUp()
                        }
                    )

                }
                is FileUploadResponse.Failure -> {
                    Snackbar.make(binding.root, response.message, Snackbar.LENGTH_SHORT).show()
                    binding.buttonUpload.isEnabled = true
                    binding.buttonUpload.text = getString(R.string.upload)
                }

                is FileUploadResponse.HttpErrorCode.Exception -> {
                    Snackbar.make(binding.root, response.exception, Snackbar.LENGTH_SHORT).show()

                    binding.buttonUpload.isEnabled = true
                    binding.buttonUpload.text = getString(R.string.upload)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (videoPlayer != null && videoPlayer!!.isPlaying) {
            videoPlayer!!.pause()
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (videoPlayer != null && videoPlayer!!.isPlaying) {
            videoPlayer!!.stop()
            videoPlayer!!.release()
        }
    }

    override fun onResume() {
        super.onResume()
        if (videoPlayer != null && !videoPlayer!!.isPlaying) {
            videoPlayer!!.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}