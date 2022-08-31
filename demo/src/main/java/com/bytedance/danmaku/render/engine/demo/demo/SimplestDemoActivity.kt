package com.bytedance.danmaku.render.engine.demo.demo

import com.bytedance.danmaku.render.engine.DanmakuView
import com.bytedance.danmaku.render.engine.control.DanmakuController
import com.bytedance.danmaku.render.engine.demo.R
import com.bytedance.danmaku.render.engine.demo.base.BaseDemoActivity


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