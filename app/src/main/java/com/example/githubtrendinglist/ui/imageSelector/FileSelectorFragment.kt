package com.example.githubtrendinglist.ui.imageSelector

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubtrendinglist.R
import com.example.githubtrendinglist.adapters.FileSelectorAdapter
import com.example.githubtrendinglist.databinding.FragmentImageSelectorBinding
import com.example.githubtrendinglist.model.GalleryModel
import com.example.githubtrendinglist.utils.AppViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal val TAG = FileSelectorFragment::class.java.simpleName

class FileSelectorFragment : Fragment(R.layout.fragment_image_selector) {


    private lateinit var viewModel: FileSelectorViewModel
    private lateinit var binding: FragmentImageSelectorBinding
    private lateinit var fileSelectorAdapter: FileSelectorAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")
        binding = FragmentImageSelectorBinding.bind(view)
        viewModel =
            ViewModelProvider(this, AppViewModelFactory())[FileSelectorViewModel::class.java]

        fileSelectorAdapter = FileSelectorAdapter()

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = fileSelectorAdapter
        }


        val requestFileStoragePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    onPermissionGranted()
                } else {
                    requestStoragePermission()
                }
            }
        if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
            requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            // request Permission
            requestFileStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            onPermissionGranted()
        }
    }

    private fun requestStoragePermission() {
        val requestFileStoragePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    onPermissionGranted()
                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        requestFileStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun onPermissionGranted() {
        lifecycleScope.launch {
            viewModel.getGalleryDataSource(requireContext()).collectLatest { pagingData ->
                Log.i(TAG, "onPermissionGranted: $pagingData")
            }
        }
    }



}
