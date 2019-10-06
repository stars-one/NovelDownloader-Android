package com.wan.noveldownloader.adapter

import android.content.Context
import com.cy.cyrvadapter.adapter.RVAdapter
import com.wan.noveldownloader.R
import com.wan.noveldownloader.model.DownloadingItem

/**
 *
 * @author StarsOne
 * @date Create in  2019/10/5 0005 22:44
 * @description
 *
 */
class DownloadingAdapter : RVAdapter<DownloadingItem> {
    private var context: Context? =null

    constructor(context: Context,list_bean: MutableList<DownloadingItem>?) : super(list_bean){
        this.context = context
    }

    override fun onItemClick(position: Int, bean: DownloadingItem?) {
    }

    override fun bindDataToView(holder: RVViewHolder?, position: Int, bean: DownloadingItem?, isSelected: Boolean) {
        holder?.setText(R.id.tv_novel_name,bean?.novelName)
        holder?.setText(R.id.tv_flag,bean?.flag)
        holder?.setText(R.id.tv_progress,bean?.progressText)
        holder?.setText(R.id.tv_progress_detail,bean?.progressDetail)
        holder?.setImage(context, R.id.iv_novel,bean?.imgUrl)
        holder?.setProgress(R.id.pb_downloading,bean!!.progress.toInt())

    }

    override fun getItemLayoutID(position: Int, bean: DownloadingItem?): Int {
        return R.layout.item_downloading
    }

}