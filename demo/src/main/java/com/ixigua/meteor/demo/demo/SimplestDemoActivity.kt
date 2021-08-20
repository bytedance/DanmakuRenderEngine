package com.ixigua.meteor.demo.demo

import com.ixigua.common.meteor.DanmakuView
import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.meteor.demo.R
import com.ixigua.meteor.demo.base.BaseDemoActivity


/**
 * Created by dss886 on 2021/04/20.
 */
class SimplestDemoActivity : BaseDemoActivity() {

    private lateinit var mController: DanmakuController

    override fun getLayoutId(): Int {
        return R.layout.activity_simplest_demo
    }

    override fun init() {
        mController = findViewById<DanmakuView>(R.id.danmaku_view).controller
        mController.setData(mDanmakuData)
        mController.start()
    }

    override fun onDestroy() {
        mController.stop()
        super.onDestroy()
    }

}