package com.example.githubtrendinglist.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubtrendinglist.model.GitRepositoryItem
import com.example.githubtrendinglist.network.Api

internal val TAG = GithubPagingSource::class.java.simpleName

class GithubPagingSource(private val api: Api, private val searchString: String) :
    PagingSource<Int, GitRepositoryItem>() {

    override fun getRefreshKey(state: PagingState<Int, GitRepositoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitRepositoryItem> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = api.searchRepositories(nextPageNumber, 25, searchString)

            return if (response.isSuccessful) {
                val data = response.body()!!
                LoadResult.Page(
                    data = data.items,
                    prevKey = null,
                    nextKey = nextPageNumber + 1
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }
}