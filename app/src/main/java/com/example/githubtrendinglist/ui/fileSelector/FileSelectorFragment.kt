package com.example.githubtrendinglist.ui.fileSelector

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubtrendinglist.R
import com.example.githubtrendinglist.adapters.FileSelectorAdapter
import com.example.githubtrendinglist.databinding.FragmentImageSelectorBinding
import com.example.githubtrendinglist.model.GalleryModel
import com.example.githubtrendinglist.utils.AppConstants
import com.example.githubtrendinglist.utils.AppInterface
import com.example.githubtrendinglist.utils.AppViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal val TAG = FileSelectorFragment::class.java.simpleName

class FileSelectorFragment : Fragment(R.layout.fragment_image_selector) {


    private lateinit var viewModel: FileSelectorViewModel
    private lateinit var binding: FragmentImageSelectorBinding
    private lateinit var fileSelectorAdapter: FileSelectorAdapter

    private val galleyItemCallback = object : AppInterface.GalleryOnItemSelect {
        override fun onItemClick(item: GalleryModel) {
            if (item.size <= AppConstants.File.FILE_LIMIT) {
                val bundle = Bundle()
                bundle.putParcelable(AppConstants.NavigationKey.FILE_KEY, item)
                findNavController().navigate(
                    R.id.action_fileSelectorFragment_to_uploadVideoFragment,
                    bundle
                )
            } else {
                Snackbar.make(binding.root, "File size too large", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImageSelectorBinding.bind(view)
        viewModel =
            ViewModelProvider(this, AppViewModelFactory())[FileSelectorViewModel::class.java]

        fileSelectorAdapter = FileSelectorAdapter(galleyItemCallback)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = fileSelectorAdapter
        }

        binding.imageButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val requestFileStoragePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    onPermissionGranted()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please enable permission in app settings to select file",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
            requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestFileStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            onPermissionGranted()
        }
    }

    private fun onPermissionGranted() {
        lifecycleScope.launch {
            viewModel.getGalleryDataSource(requireContext()).collectLatest { pagingData ->
                fileSelectorAdapter.submitData(pagingData)
            }
        }
    }
}
