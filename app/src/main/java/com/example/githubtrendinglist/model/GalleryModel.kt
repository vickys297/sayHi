package com.example.githubtrendinglist.model

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import java.io.Serializable


data class GalleryModel(
    val id: Long,
    val name: String,
    val size: Float,
    val createdAt: String,
    val fileDataType: Int,
    val mimeType: String,
    val contentUri: String,
    var isSelected: Boolean = false,
    val fileData: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeFloat(size)
        parcel.writeString(createdAt)
        parcel.writeInt(fileDataType)
        parcel.writeString(mimeType)
        parcel.writeString(contentUri)
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeString(fileData)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GalleryModel> {
        override fun createFromParcel(parcel: Parcel): GalleryModel {
            return GalleryModel(parcel)
        }

        override fun newArray(size: Int): Array<GalleryModel?> {
            return arrayOfNulls(size)
        }
    }


}