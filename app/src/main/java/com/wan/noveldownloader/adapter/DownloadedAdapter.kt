package com.wan.noveldownloader.adapter

import android.content.Context
import com.cy.cyrvadapter.adapter.RVAdapter
import com.wan.noveldownloader.R
import com.wan.noveldownloader.model.DownloadedItem

/**
 *
 * @author StarsOne
 * @date Create in  2019/10/5 0005 22:44
 * @description
 *
 */
class DownloadedAdapter(list_bean: MutableList<DownloadedItem>?) : RVAdapter<DownloadedItem>(list_bean) {
    private var context: Context? =null
    override fun getItemLayoutID(position: Int, bean: DownloadedItem?): Int {
        return R.layout.item_downloaded
    }

    override fun bindDataToView(holder: RVViewHolder?, position: Int, bean: DownloadedItem?, isSelected: Boolean) {
        holder?.setText(R.id.tv_novel_name,bean?.novelName)
        holder?.setText(R.id.tv_file_size,bean?.fileSize)
        holder?.setText(R.id.tv_file_path,bean?.filePath)
        holder?.setImage(holder.itemView.context, R.id.iv_novel,bean?.imgUrl)
    }

    override fun onItemClick(position: Int, bean: DownloadedItem?) {
    }



}