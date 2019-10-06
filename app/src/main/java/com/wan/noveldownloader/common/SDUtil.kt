package com.wan.noveldownloader.common

import android.content.Context

/**
 *
 * @author StarsOne
 * @date Create in  2019/10/6 0006 15:32
 * @description
 *
 */
class SDUtil {
    companion object {
        /**
         * 通过反射调用获取内置存储和外置sd卡根路径(通用)
         *
         * @param mContext    上下文
         * @param is_removale 是否可移除，false返回内部存储路径，true返回外置SD卡路径
         * @return
         */
        fun getStoragePath(mContext: Context, is_removale: Boolean): String {
            //使用getSystemService(String)检索一个StorageManager用于访问系统存储功能。
            val mStorageManager = mContext.getSystemService(Context.STORAGE_SERVICE)

            val storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
            val getVolumeList = mStorageManager.javaClass.getMethod("getVolumeList")
            val getPath = storageVolumeClazz.getMethod("getPath")
            val isRemovable = storageVolumeClazz.getMethod("isRemovable")
            val result = getVolumeList.invoke(mStorageManager)
            for (i in 0 until java.lang.reflect.Array.getLength(result)) {
                val element = java.lang.reflect.Array.get(result, i)
                val path = getPath.invoke(element)
                val removable = isRemovable.invoke(element)
                if (removable == is_removale) return path.toString()
            }
            return ""
        }
    }
}