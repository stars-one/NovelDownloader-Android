package com.wan.noveldownloader.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 *
 * @author StarsOne
 * @date Create in  2019/10/8 0008 19:26
 * @description
 *
 */
class FragmentAdapter: FragmentPagerAdapter {
    private var list: ArrayList<Fragment> = arrayListOf()

    constructor(fm: FragmentManager?, list: ArrayList<Fragment>) : super(fm) {
        this.list = list
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (position == 0) {
            "下载中"
        } else {
            "已下载"
        }
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }
}