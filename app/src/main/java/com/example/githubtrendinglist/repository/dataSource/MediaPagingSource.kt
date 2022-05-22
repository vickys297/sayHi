package com.example.githubtrendinglist.repository.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubtrendinglist.api.MediaAPI
import com.example.githubtrendinglist.model.Datum

class MediaListPagingSource(private val mediaAPI: MediaAPI) :
    PagingSource<Int, Datum>() {

    override fun getRefreshKey(state: PagingState<Int, Datum>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Datum> {
        try {
            val nextPageNumber = params.key ?: 0
            val response = mediaAPI.getMediaList(nextPageNumber.toString())

            return if (response.isSuccessful) {
                val data = response.body()!!.data
                LoadResult.Page(
                    data = data!!,
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