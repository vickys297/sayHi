package com.example.githubtrendinglist.ui.imageSelector

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubtrendinglist.model.GalleryModel
import com.example.githubtrendinglist.repository.AppRepository
import com.example.githubtrendinglist.repository.dataSource.GalleryDataSource
import kotlinx.coroutines.flow.Flow

class FileSelectorViewModel(private val repository: AppRepository) : ViewModel() {



    init {
        Log.i(TAG, "GalleryListViewModel: init")

    }


    fun getGalleryDataSource(context:Context): Flow<PagingData<GalleryModel>> {
        val flow: Flow<PagingData<GalleryModel>> = Pager(
            PagingConfig(
                pageSize = 25,
                enablePlaceholders = false
            )
        ) {
            GalleryDataSource(context)
        }.flow
            .cachedIn(viewModelScope)
        return flow
    }
}