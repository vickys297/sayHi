package com.example.githubtrendinglist.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubtrendinglist.model.Datum
import com.example.githubtrendinglist.model.FileUploadResponse
import com.example.githubtrendinglist.model.VideoModelData
import com.example.githubtrendinglist.repository.AppRepository
import kotlinx.coroutines.flow.Flow

internal val TAG = RepositoryListViewModel::class.java.simpleName

class RepositoryListViewModel(private val repository: AppRepository) : ViewModel() {


    fun getMediaList(): Flow<PagingData<Datum>> {
        return repository.getMediaListRepository().cachedIn(viewModelScope)
    }

    suspend fun deleteMediaItem(id: Int): FileUploadResponse {
       return repository.deleteMediaItem(id.toString())
    }

}