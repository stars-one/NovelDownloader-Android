package com.wan.noveldownloader.common

import android.app.Activity
import android.didikee.donate.AlipayDonate
import android.didikee.donate.WeiXinDonate
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.widget.Toast
import com.wan.noveldownloader.R
import java.io.File


/**
 *
 * @author StarsOne
 * @date Create in  2019/10/4 0004 23:15
 * @description
 *
 */
class DonateUtil {
    companion object {
        /**
         * 支付宝捐赠
         *
         * @param payCode 收款码后面的字符串；例如：收款二维码里面的字符串为 https://qr.alipay.com/stx00187oxldjvyo3ofaw60 ，则
         *                payCode = stx00187oxldjvyo3ofaw60
         *                注：不区分大小写
         */
        fun donateAlipay(activity: Activity) {
            val hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(activity)
            if (hasInstalledAlipayClient) {
                AlipayDonate.startAlipayClient(activity, "fkx09316opdsamnwrivcud2")
            } else {
                Toast.makeText(activity, "您的手机未安装支付宝哦", Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * 微信捐赠
         */
        fun donateWeixin(activity: Activity) {
            val qrPath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "AndroidDonateSample" + File.separator +
                    "weixin.png"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val bitmap = BitmapFactory.decodeResource(activity.resources, R.drawable.weixin)
                WeiXinDonate.saveDonateQrImage2SDCard(qrPath,bitmap)
                WeiXinDonate.donateViaWeiXin(activity,qrPath)
            }

        }
    }
}