package com.wan.noveldownloader.activity

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.View
import com.wan.noveldownloader.R
import com.wan.noveldownloader.common.BarUtil
import com.wan.noveldownloader.common.DonateUtil
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v) {
            con_about -> showToast("关于界面")
            con_donate -> {
                AlertDialog.Builder(this).setTitle("打赏").setMessage("若是觉得软件对你有所帮助，不妨打赏支持我一波！你的打赏，就是我的更新的动力")
                        .setPositiveButton("支付宝", { dialog, which ->
                            run {
                                DonateUtil.donateAlipay(this)
                                dialog.cancel()
                            }
                        }).create().show()

            }
            con_setting-> showToast("设置界面")
        }
    }

    override fun initData() {
    }

    override fun initView() {
        con_about.setOnClickListener(this)
        con_donate.setOnClickListener(this)
        con_setting.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtil.setStatusBarColor(this, getColor(R.color.colorPrimary))
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}
