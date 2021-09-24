package com.ixigua.meteor.demo.demo

import android.graphics.PointF
import android.graphics.RectF
import androidx.core.graphics.PathParser
import com.ixigua.common.meteor.DanmakuView
import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.touch.IItemClickListener
import com.ixigua.common.meteor.utils.CMD_PAUSE_ITEM
import com.ixigua.common.meteor.utils.CMD_RESUME_ITEM
import com.ixigua.common.meteor.utils.DRAW_ORDER_DEFAULT
import com.ixigua.meteor.demo.R
import com.ixigua.meteor.demo.base.BaseDemoActivity
import com.ixigua.meteor.demo.demo.mask.MaskDanmakuFactory
import com.ixigua.meteor.demo.demo.mask.MaskData
import com.ixigua.meteor.demo.demo.mask.MaskLayer


/**
 * Created by dss886 on 2021/08/19.
 */
class SmartMaskActivity: BaseDemoActivity() {

    private lateinit var mController: DanmakuController
    private var mPausingItem: DanmakuData? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_smart_mask
    }

    override fun init() {
        mController = findViewById<DanmakuView>(R.id.danmaku_view).controller
        mController.registerDrawItemFactory(MaskDanmakuFactory())
        mController.addRenderLayer(MaskLayer())

        /**
         * Load SVG path data to [android.graphics.Path],
         * same as the pathData in [R.drawable.mask_demo]
         */
        val path = PathParser.createPathFromPathData("M220 360 q-10 -100 50 -200 a60 60 0 1 1 100 0 q60 100 50 200 z")
        mDanmakuData = mDanmakuData.toMutableList().apply {
            add(0, MaskData().also {
                it.path = path
                it.pathWidth = 640
                it.pathHeight = 360
                /**
                 * We only use a single mask that lasts 1 minute to demonstrate here.
                 * In production environment, the frame rate of the mask can reach
                 * 30Hz-60Hz according to the performance of the device.
                 */
                it.start = 0L
                it.end = 60_000L
            })
        }

        mController.itemClickListener = object : IItemClickListener {
            override fun onDanmakuClick(data: DanmakuData, itemRect: RectF, clickPoint: PointF) {
                mPausingItem = if (mPausingItem == null) {
                    mController.executeCommand(CMD_PAUSE_ITEM, data)
                    data.drawOrder = Integer.MAX_VALUE
                    data
                } else {
                    mController.executeCommand(CMD_RESUME_ITEM, mPausingItem)
                    data.drawOrder = DRAW_ORDER_DEFAULT
                    null
                }
            }
        }

        mController.setData(mDanmakuData)
        mController.config.scroll.lineCount = 6
        mController.config.mask.enable = true
        mController.start()
    }

    override fun onDestroy() {
        mController.stop()
        super.onDestroy()
    }

}