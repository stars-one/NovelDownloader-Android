package com.wan.noveldownloader.activity

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.wan.noveldownloader.R
import com.wan.noveldownloader.adapter.FragmentAdapter
import com.wan.noveldownloader.common.BarUtil
import com.wan.noveldownloader.fragment.DownloadedFragment
import com.wan.noveldownloader.fragment.DownloadingFragment
import com.wan.noveldownloader.model.DownloadedItem
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_main.*
import org.litepal.LitePal


class MainActivity : BaseActivity(), View.OnClickListener {

    private val downloadedFragment = DownloadedFragment()
    override fun onClick(v: View?) {
        /*when (v) {

        }*/
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtil.setStatusBarColor(this, getColor(R.color.colorPrimary))
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //创建数据库
        LitePal.getDatabase()
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


    fun addItemToHistory(downloadedItem: DownloadedItem?) {
        downloadedItem?.let { downloadedFragment.addItem(it) }
    }

    override fun initView() {
        val fragments = arrayListOf(DownloadingFragment(), downloadedFragment)
        val fm = supportFragmentManager
        viewpager.adapter = FragmentAdapter(fm, fragments)
        tablayout.setupWithViewPager(viewpager)

    }


}
