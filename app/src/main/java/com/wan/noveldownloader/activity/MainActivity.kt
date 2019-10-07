package com.wan.noveldownloader.activity

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SimpleItemAnimator
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.cy.cyrvadapter.adapter.RVAdapter
import com.wan.noveldownloader.R
import com.wan.noveldownloader.common.BarUtil
import com.wan.noveldownloader.common.NovelDownloadTool
import com.wan.noveldownloader.model.DownloadingItem
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class MainActivity : BaseActivity(), View.OnClickListener {
    //允许在同一时刻有5个任务正在执行，并且最多能够存储50个任务
    private val exec = ThreadPoolExecutor(5, 50, 10, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())

    private var adapter: RVAdapter<DownloadingItem>? = null
    private val dataList = arrayListOf<DownloadingItem>()

    internal inner class DownloadingTask : AsyncTask<String, DownloadingItem, Int>() {
        var isFirst = true
        override fun doInBackground(vararg params: String?): Int {
            val tool = NovelDownloadTool(params[0].toString(), adapter!!.list_bean.size)
            val messageItem = tool.getMessage()
            publishProgress(messageItem)
            for (i in 0 until tool.chacterMap.size) {
                //下载每章节，并更新
                val item = tool.downloadChacter(this@MainActivity, i)
                publishProgress(item)
            }
            return tool.mergeFile(this@MainActivity)
        }

        override fun onProgressUpdate(vararg values: DownloadingItem?) {
            //recyclerView Item更新
            if (isFirst) {
                values[0]?.let { dataList.add(it) }
                adapter?.notifyDataSetChanged()
                isFirst = false
            } else {
                updateItem(values.last())
            }
        }

        override fun onPostExecute(result: Int?) {
            showToast("下载成功")
            //移出adapter中的数据
            result?.let { adapter!!.remove(it) }
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            btn_add -> {
                val editText = TextInputEditText(this)
                AlertDialog.Builder(this).setTitle("输入小说网址")
                        .setView(editText)
                        .setPositiveButton("确定", { dialog, which -> startTask(editText.text.toString()) })
                        .setNegativeButton("取消", { dialog, which -> dialog.cancel() })
                        .create().show()
            }
        }
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtil.setStatusBarColor(this, getColor(R.color.colorPrimary))
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //存储权限申请
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted({ permissions ->

                })
                .start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_setting) {
            startActivity(MenuActivity::class.java)
        }
        return false
    }

    override fun initData() {
    }

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

    override fun initView() {
        btn_add.setOnClickListener(this)

        adapter = object : RVAdapter<DownloadingItem>(dataList) {
            override fun onItemClick(position: Int, bean: DownloadingItem?) {
            }

            override fun bindDataToView(holder: RVViewHolder?, position: Int, bean: DownloadingItem?, isSelected: Boolean) {
                holder?.setText(R.id.tv_novel_name, bean?.novelName)
                holder?.setText(R.id.tv_flag, bean?.flag)
                holder?.setText(R.id.tv_progress, bean?.progressText)
                holder?.setText(R.id.tv_progress_detail, bean?.progressDetail)
                holder?.setImage(this@MainActivity.applicationContext, R.id.iv_novel, bean?.imgUrl)
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


}
