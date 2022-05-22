package com.example.githubtrendinglist.ui.viewPager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubtrendinglist.model.Datum
import com.example.githubtrendinglist.model.MediaListResponse
import com.example.githubtrendinglist.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class ViewPagerViewModel(private val repository: AppRepository) : ViewModel() {

    val mutableMediaList = MutableLiveData<List<Datum>>()

    fun getMediaList(): Flow<PagingData<Datum>> {
        return repository.getMediaListRepository().cachedIn(viewModelScope)
    }

    suspend fun getFixedMediaList(lastItemId: String): MediaListResponse {
        return repository.getFixedSizeMediaList(lastItemId = lastItemId)
    }
}