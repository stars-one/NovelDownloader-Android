package com.wan.noveldownloader.fragment

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.cy.cyrvadapter.adapter.RVAdapter
import com.wan.noveldownloader.R
import com.wan.noveldownloader.common.NovelDownloadTool
import com.wan.noveldownloader.model.DownloadingItem
import kotlinx.android.synthetic.main.fragment_downloading.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class DownloadingFragment : Fragment() {

    //允许在同一时刻有5个任务正在执行，并且最多能够存储50个任务
    private val exec = ThreadPoolExecutor(5, 50, 10, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())

    private var adapter: RVAdapter<DownloadingItem>? = null
    private val dataList = arrayListOf<DownloadingItem>()

    internal inner class DownloadingTask : AsyncTask<String, DownloadingItem, Int>() {
        var isFirst = true
        var itemPosition = 0
        var tvStatus: TextView? = null
        override fun onPreExecute() {
            //一些初始化操作
            itemPosition = adapter!!.list_bean.size

        }

        override fun doInBackground(vararg params: String?): Int {

            val tool = NovelDownloadTool(params[0].toString(), itemPosition)
            val messageItem = tool.getMessage()
            publishProgress(messageItem)
            for (i in 0 until tool.chacterMap.size) {
                //下载每章节，并更新
                val item = tool.downloadChacter(this@DownloadingFragment.activity, i)
                publishProgress(item)

                //tvStatus控件可能为空（因为RecyclerView的itemView未初始化成功)
                while (tvStatus?.text.toString() != "1") {
                }
                // if (tvStatus != null) while (tvStatus!!.text.toString() != "1"){}
            }
            return tool.mergeFile(this@DownloadingFragment.activity)
        }

        override fun onProgressUpdate(vararg values: DownloadingItem?) {
            //recyclerView Item更新
            if (isFirst) {
                values[0]?.let { dataList.add(it) }
                adapter?.notifyDataSetChanged()
                isFirst = false

            } else {
                updateItem(values.last())
                if (tvStatus == null) {
                    tvStatus = rv_downloading.findViewHolderForAdapterPosition(itemPosition).itemView.findViewById(R.id.tv_status) as TextView?
                }
            }
        }

        override fun onPostExecute(result: Int?) {
            showToast("下载成功")
            //移出adapter中的数据
            result?.let { adapter!!.remove(it) }
        }

    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_downloading, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    fun initView() {
        btn_add.setOnClickListener({
            val editText = TextInputEditText(this.activity)
            AlertDialog.Builder(this.activity).setTitle("输入小说网址")
                    .setView(editText)
                    .setPositiveButton("确定", { dialog, which -> startTask(editText.text.toString()) })
                    .setNegativeButton("取消", { dialog, which -> dialog.cancel() })
                    .create().show()

        })
        adapter = object : RVAdapter<DownloadingItem>(dataList) {
            override fun onItemClick(position: Int, bean: DownloadingItem?) {
            }

            override fun bindDataToView(holder: RVViewHolder?, position: Int, bean: DownloadingItem?, isSelected: Boolean) {
                holder?.setText(R.id.tv_novel_name, bean?.novelName)
                holder?.setText(R.id.tv_flag, bean?.flag)
                holder?.setText(R.id.tv_progress, bean?.progressText)
                holder?.setText(R.id.tv_progress_detail, bean?.progressDetail)
                holder?.setImage(this@DownloadingFragment.activity.applicationContext, R.id.iv_novel, bean?.imgUrl)
                holder?.setProgress(R.id.pb_downloading, bean!!.progress.toInt())

                holder?.setOnClickListener(R.id.btn_pause, {
                    val tvStatus = holder.getView<TextView>(R.id.tv_status)
                    val btnPause = holder.getView<Button>(R.id.btn_pause)
                    //暂停按钮点击会切换图标
                    if (tvStatus.text.toString() == "1") {
                        //不同android版本获取drawble的方法不一样
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            btnPause.background = resources.getDrawable(R.drawable.icon_start, null)
                        } else {
                            btnPause.background = resources.getDrawable(R.drawable.icon_start)
                        }
                        tvStatus.text = "0"
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            btnPause.background = resources.getDrawable(R.drawable.icon_pause, null)
                        } else {
                            btnPause.background = resources.getDrawable(R.drawable.icon_pause)
                        }
                        tvStatus.text = "1"
                    }
                })
            }

            override fun getItemLayoutID(position: Int, bean: DownloadingItem?): Int {
                return R.layout.item_downloading
            }
        }
        rv_downloading.adapter = adapter
        //关闭动画，解决RecyclerView滑动闪烁
        val itemAnimator = rv_downloading.itemAnimator
        if (itemAnimator is SimpleItemAnimator) itemAnimator.supportsChangeAnimations = false
    }

    /**
     * 更新进度等信息
     */
    fun updateItem(downloadingItem: DownloadingItem?) {
        val viewholder = rv_downloading.findViewHolderForAdapterPosition(downloadingItem!!.itemPosition)
        val itemView = viewholder.itemView
        //进度条刷新进度
        val pb = itemView.findViewById(R.id.pb_downloading)
        if (pb is ProgressBar) pb.progress = downloadingItem.progress.toInt()
        //百分比进度
        val pbText = itemView.findViewById(R.id.tv_progress)
        if (pbText is TextView) pbText.text = downloadingItem.progressText
        //具体进度
        val tvDetail = itemView.findViewById(R.id.tv_progress_detail)
        if (tvDetail is TextView) tvDetail.text = downloadingItem.progressDetail
        //状态
        val tvFlag = itemView.findViewById(R.id.tv_flag)
        if (tvFlag is TextView) tvFlag.text = downloadingItem.flag

    }

    /**
     * 开始下载小说
     */
    fun startTask(url: String) {
        if (url.contains("www.x23qb.com")) {
            showToast("解析网址中，请稍候...")
            DownloadingTask().executeOnExecutor(exec, url)
        } else {
            showToast("不支持该书源哦！")
        }
    }

    /**
     * 显示Toast
     */
    fun showToast(text: String) {
        Toast.makeText(this.activity, text, Toast.LENGTH_SHORT).show()
    }



}