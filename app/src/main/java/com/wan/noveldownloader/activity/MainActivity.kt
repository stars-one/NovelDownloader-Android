package com.wan.noveldownloader.activity

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.wan.noveldownloader.R
import com.wan.noveldownloader.common.BarUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(),View.OnClickListener {
    override fun onClick(v: View?) {
        when (v) {
            btn_add -> showToast("helo")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtil.setStatusBarColor(this,getColor(R.color.colorPrimary))
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId ==R.id.menu_setting) {
            startActivity(MenuActivity::class.java)
        }
        return false
    }

    override fun initData() {
    }

    override fun initView() {
        btn_add.setOnClickListener(this)
    }

}
