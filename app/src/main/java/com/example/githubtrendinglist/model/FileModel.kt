package com.example.githubtrendinglist.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class FileModel(
    val id: Long,
    val name: String,
    val fileDataType: Int,
    val size: Long,
    val createdAt: String?,
    val mimeType: String,
    val contentUri: Uri?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString()!!,
        parcel.readParcelable(Uri::class.java.classLoader)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeInt(fileDataType)
        parcel.writeLong(size)
        parcel.writeString(createdAt)
        parcel.writeString(mimeType)
        parcel.writeParcelable(contentUri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FileModel> {
        override fun createFromParcel(parcel: Parcel): FileModel {
            return FileModel(parcel)
        }

        override fun newArray(size: Int): Array<FileModel?> {
            return arrayOfNulls(size)
        }
    }

}