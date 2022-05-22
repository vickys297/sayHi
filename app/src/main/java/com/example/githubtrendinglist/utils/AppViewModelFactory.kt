package com.example.githubtrendinglist.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubtrendinglist.repository.AppRepository
import com.example.githubtrendinglist.ui.imageSelector.FileSelectorViewModel
import com.example.githubtrendinglist.ui.RepositoryListViewModel
import com.example.githubtrendinglist.ui.fullScreenView.FullScreenMediaViewViewModel
import com.example.githubtrendinglist.ui.uploadVideo.UploadVideoViewModel
import com.example.githubtrendinglist.ui.viewPager.ViewPagerViewModel

class AppViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = AppRepository()

        return when (modelClass) {
            RepositoryListViewModel::class.java -> {
                RepositoryListViewModel(repository) as T
            }
            ViewPagerViewModel::class.java -> {
                ViewPagerViewModel(repository) as T
            }
            FullScreenMediaViewViewModel::class.java -> {
                FullScreenMediaViewViewModel(repository) as T
            }
            UploadVideoViewModel::class.java -> {
                UploadVideoViewModel(repository) as T
            }
            FileSelectorViewModel::class.java -> {
                FileSelectorViewModel(repository) as T
            }
            else -> {
                throw Exception("View model not found")
            }
        }
    }
}