package com.example.githubtrendinglist.repository.dataSource

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns
import android.provider.MediaStore.Files.getContentUri
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubtrendinglist.model.GalleryModel
import com.example.githubtrendinglist.model.GalleryPagingModel

internal val TAG = GalleryDataSource::class.java.canonicalName

class GalleryDataSource internal constructor(private val context: Context) :
    PagingSource<Int, GalleryModel>() {


    override fun getRefreshKey(state: PagingState<Int, GalleryModel>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryModel> {

        try {
            val nextPageNumber = params.key ?: 0
            val response = getGalleryData(nextPageNumber)
            Log.d(TAG, "load: ${response.data.size}")

            return LoadResult.Page(
                data = response.data,
                prevKey = null, // Only paging forward.
                nextKey = null
            )

        } catch (e: Exception) {
            throw e
        }

    }


    @WorkerThread
    private fun getGalleryData(nextPageNumber: Int): GalleryPagingModel {

        val startTime = System.currentTimeMillis()

        val galleryModelArrayList = ArrayList<GalleryModel>()

        // Get relevant columns for use later.
        val projection = arrayOf(
            FileColumns._ID,
            FileColumns.DATE_ADDED,
            FileColumns.MEDIA_TYPE,
            FileColumns.MIME_TYPE,
            FileColumns.TITLE,
            FileColumns.SIZE,
            FileColumns.VOLUME_NAME,
            FileColumns.DISPLAY_NAME,
        )

        // Return only video and image metadata.
        // Return only video and image metadata.
        val selection = (FileColumns.MEDIA_TYPE + "="
                + FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + FileColumns.MEDIA_TYPE + "="
                + FileColumns.MEDIA_TYPE_VIDEO)

        val queryUri: Uri = getContentUri(MediaStore.VOLUME_EXTERNAL)

        val sorter = "${FileColumns.DATE_ADDED} DESC"

        val cursor = context.contentResolver.query(
            queryUri,
            projection,
            selection,
            null,
            sorter
        )

        cursor?.use {

            val columnId = cursor.getColumnIndexOrThrow(FileColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(FileColumns.DISPLAY_NAME)
            val fileSize = cursor.getColumnIndexOrThrow(FileColumns.SIZE)
            val createdDate = cursor.getColumnIndexOrThrow(FileColumns.DATE_ADDED)
            val fileType = cursor.getColumnIndexOrThrow(FileColumns.MEDIA_TYPE)
            val mimeType = cursor.getColumnIndexOrThrow(FileColumns.MIME_TYPE)

            Log.d(
                TAG,
                "getGalleryData: ${cursor.getColumnIndexOrThrow(FileColumns.VOLUME_NAME)}"
            )

            while (cursor.moveToNext()) {

                val id = cursor.getLong(columnId)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(fileSize)
                val createdAt = cursor.getString(createdDate)
                val fileDataType = cursor.getInt(fileType)
                val mimeType = cursor.getString(mimeType)


                val contentUri = if (fileDataType == FileColumns.MEDIA_TYPE_VIDEO) {
                    ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                } else {
                    ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                }

                val newData = GalleryModel(
                    id = id,
                    name = name,
                    size = size,
                    createdAt = createdAt,
                    fileDataType = fileDataType,
                    mimeType = mimeType,
                    contentUri = contentUri.toString()
                )
                galleryModelArrayList.add(newData)
            }

            cursor.close()
        }

        val endTime = System.currentTimeMillis() - startTime
        Log.i(TAG, "getGalleryData: time in long $endTime Ms")

        return GalleryPagingModel(galleryModelArrayList)
    }
}