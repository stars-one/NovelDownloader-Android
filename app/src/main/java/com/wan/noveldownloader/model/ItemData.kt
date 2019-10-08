package com.wan.noveldownloader.model

import android.os.Parcel
import android.os.Parcelable
import org.litepal.crud.LitePalSupport


/**
 *
 * @author StarsOne
 * @date Create in  2019/10/5 0005 10:58
 * @description
 *
 */

/**
 * @param progress：进度条的进度 int
 * @param progressText：进度 25.65% String
 * @param progressDetail： 具体进度  20/52 String
 */
data class DownloadingItem(var novelName: String, var imgUrl: String, var progress: Double, var progressText: String, var progressDetail: String, var flag: String, var itemPosition: Int)

/**
 * fileSize:文件大小
 */
data class DownloadedItem(var novelName: String, var imgUrl: String, var fileSize: String): LitePalSupport(),Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(novelName)
        parcel.writeString(imgUrl)
        parcel.writeString(fileSize)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DownloadedItem> {
        override fun createFromParcel(parcel: Parcel): DownloadedItem {
            return DownloadedItem(parcel)
        }

        override fun newArray(size: Int): Array<DownloadedItem?> {
            return arrayOfNulls(size)
        }
    }
}