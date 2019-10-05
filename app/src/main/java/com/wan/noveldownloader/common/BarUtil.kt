package com.wan.noveldownloader.common

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.v4.graphics.ColorUtils
import android.view.View
import android.view.WindowManager
import com.readystatesoftware.systembartint.SystemBarTintManager
import com.wan.noveldownloader.R


/**
 *
 * @author StarsOne
 * @date Create in  2019/10/3 0003 19:04
 * @description
 *
 */
class BarUtil {
    companion object {


        /**
         * 修改状态栏颜色
         */
        fun setStatusBarColor(activity: Activity, color: Int) {

            var isLightColor = false
            val v = ColorUtils.calculateLuminance(color)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.getWindow()
                //取消设置Window半透明的Flag
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                //添加Flag把状态栏设为可绘制模式
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                //设置状态栏为颜色
                window.setStatusBarColor(color)
            }

            //判断颜色是否亮色
            if (v >= 0.5) {
                isLightColor = true
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isLightColor) {
                    //设置黑色字体颜色
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    //设置白色字体颜色
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
            }
        }

        /**
         * 设置状态栏为白色，图标为黑色
         */
        fun setWhiteBar(activity: Activity) {
            //设置状态栏为全透明，没有此步会状态栏会显示灰色
            transparencyBar(activity)
            //设置状态栏为白色
            val tintManager = SystemBarTintManager(activity)
            tintManager.isStatusBarTintEnabled = true
            tintManager.setStatusBarTintResource(R.color.white)
            //设置状态栏字体和图标为黑色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        /**
         * 修改状态栏为全透明
         *
         * @param activity
         */
        @TargetApi(19)
        fun transparencyBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.window
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val window = activity.window
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
    }


}