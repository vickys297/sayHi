package com.example.githubtrendinglist.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubtrendinglist.model.GitRepositoryItem
import com.example.githubtrendinglist.network.Api
import com.example.githubtrendinglist.network.RetrofitServices
import com.example.githubtrendinglist.repository.GithubPagingSource
import kotlinx.coroutines.flow.Flow

internal val TAG = RepositoryListViewModel::class.java.simpleName

class RepositoryListViewModel : ViewModel() {
    fun getRepositoryList(): Flow<PagingData<GitRepositoryItem>> {
        Log.i(TAG, "getRepositoryList: ")
        val api = RetrofitServices.getInstance().createService(Api::class.java)
        val flow = Pager(
            PagingConfig(pageSize = 20)
        ) {
            GithubPagingSource(api, "a")
        }.flow
            .cachedIn(viewModelScope)
        return flow
    }
}