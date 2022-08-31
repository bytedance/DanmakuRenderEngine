package com.bytedance.danmaku.render.engine.demo.demo

import android.graphics.PointF
import android.graphics.RectF
import androidx.core.graphics.PathParser
import com.bytedance.danmaku.render.engine.DanmakuView
import com.bytedance.danmaku.render.engine.control.DanmakuController
import com.bytedance.danmaku.render.engine.data.DanmakuData
import com.bytedance.danmaku.render.engine.render.draw.mask.MaskData
import com.bytedance.danmaku.render.engine.touch.IItemClickListener
import com.bytedance.danmaku.render.engine.utils.CMD_PAUSE_ITEM
import com.bytedance.danmaku.render.engine.utils.CMD_RESUME_ITEM
import com.bytedance.danmaku.render.engine.utils.DRAW_ORDER_DEFAULT
import com.bytedance.danmaku.render.engine.demo.R
import com.bytedance.danmaku.render.engine.demo.base.BaseDemoActivity

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