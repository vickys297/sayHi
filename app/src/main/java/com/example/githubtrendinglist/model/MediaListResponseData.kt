package com.example.githubtrendinglist.model

import android.os.Parcel
import android.os.Parcelable


data class MediaListResponseData(
    val statusCode: Long,
    val message: String?,
    val data: ArrayList<Datum>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.createTypedArrayList(Datum.CREATOR)
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(statusCode)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaListResponseData> {
        override fun createFromParcel(parcel: Parcel): MediaListResponseData {
            return MediaListResponseData(parcel)
        }

        override fun newArray(size: Int): Array<MediaListResponseData?> {
            return arrayOfNulls(size)
        }
    }

}

data class Datum(
    val id: Int,
    val file: String?,
    val file_type: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(file)
        parcel.writeInt(file_type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Datum> {
        override fun createFromParcel(parcel: Parcel): Datum {
            return Datum(parcel)
        }

        override fun newArray(size: Int): Array<Datum?> {
            return arrayOfNulls(size)
        }
    }

}


sealed class MediaListResponse {
    data class Success(val result: List<Datum>) : MediaListResponse()
    data class Failure(val message: String) : MediaListResponse()

    sealed class HttpErrorCode : MediaListResponse() {
        data class Exception(val exception: String) : MediaListResponse()
    }
}