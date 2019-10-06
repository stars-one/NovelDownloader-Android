package com.wan.noveldownloader.activity

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.MenuItem
import com.wan.noveldownloader.R
import com.wan.noveldownloader.common.BarUtil
import com.wan.noveldownloader.common.DonateUtil
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.donate.*


class AboutActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtil.setStatusBarColor(this, getColor(R.color.colorPrimary))
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initData() {

    }

    override fun initView() {
        donateBtn.setOnClickListener({ DonateUtil.donateAlipay(this) })
    }
}