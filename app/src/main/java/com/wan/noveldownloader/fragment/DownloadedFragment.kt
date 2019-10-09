package com.wan.noveldownloader.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cy.cyrvadapter.adapter.RVAdapter
import com.wan.noveldownloader.R
import com.wan.noveldownloader.adapter.DownloadedAdapter
import com.wan.noveldownloader.model.DownloadedItem
import kotlinx.android.synthetic.main.fragment_downloaded.*
import org.litepal.LitePal
import org.litepal.extension.findAll

class DownloadedFragment : Fragment() {

    private var list: MutableList<DownloadedItem>? = null
    private var adapter: RVAdapter<DownloadedItem>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_downloaded, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {
        //数据库有数据，从数据库读取
        list = if (LitePal.isExist(DownloadedItem::class.java)) {
            LitePal.findAll<DownloadedItem>()
        } else {
            mutableListOf()
        }
    }

    private fun initView() {
        btn_delete.setOnClickListener { delete() }
        adapter = DownloadedAdapter(list)
        rv_downloaded.adapter = adapter
    }

    fun addItem(downloadedItem: DownloadedItem) {
        list?.add(downloadedItem)
        adapter?.notifyDataSetChanged()
        downloadedItem.save()
    }

    private fun delete() {
        list = arrayListOf()
        adapter?.notifyDataSetChanged()
    }
}