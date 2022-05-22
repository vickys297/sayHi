package com.example.githubtrendinglist.ui.uploadVideo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.githubtrendinglist.R
import com.example.githubtrendinglist.databinding.FragmentUploadVideoBinding
import com.example.githubtrendinglist.model.FileModel
import com.example.githubtrendinglist.utils.AppConstants
import com.example.githubtrendinglist.utils.AppViewModelFactory
import com.iceteck.silicompressorr.SiliCompressor
import java.io.File

internal val TAG = UploadVideoFragment::class.java.simpleName

class UploadVideoFragment : Fragment(R.layout.fragment_upload_video) {

    companion object {
        fun newInstance() = UploadVideoFragment()
    }

    private lateinit var viewModel: UploadVideoViewModel
    private lateinit var binding: FragmentUploadVideoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUploadVideoBinding.bind(view)
        viewModel = ViewModelProvider(this, AppViewModelFactory())[UploadVideoViewModel::class.java]

        requireArguments().getParcelable<FileModel>(AppConstants.NavigationKey.FILE_KEY)?.let {

            val destinationPath = File("/storage/emulated/0/SimplyScial/video")
            if (!destinationPath.exists()) {
                destinationPath.mkdirs()
            }
            val file = File(destinationPath.absolutePath)
            Log.i(TAG, "onViewCreated:  ${file.absolutePath}")
            val filePath: String =
                SiliCompressor.with(requireContext())
                    .compressVideo(it.contentUri, file.absolutePath)

            Log.i(TAG, "onViewCreated: $filePath")
        }
    }
}