package com.bytedance.danmaku.render.engine.demo.demo.layer.meteor

import android.graphics.Canvas
import com.bytedance.danmaku.render.engine.control.DanmakuController
import com.bytedance.danmaku.render.engine.data.DanmakuData
import com.bytedance.danmaku.render.engine.render.IRenderLayer
import com.bytedance.danmaku.render.engine.render.draw.DrawItem
import com.bytedance.danmaku.render.engine.render.layer.line.BaseRenderLine
import com.bytedance.danmaku.render.engine.utils.HIGH_REFRESH_MAX_TIME
import com.bytedance.danmaku.render.engine.utils.STEPPER_TIME
import kotlin.math.sqrt

/**
 * Created by dss886 on 2021/08/19.
 */
class MeteorLine (controller: DanmakuController,
                  private val mLayer: IRenderLayer) : BaseRenderLine(controller, mLayer) {

    private var mLastTypeSettingTime = -1L
    private var mStepperTime = STEPPER_TIME
    private var mRotateDegree = -45F
    private val mSqrt2 = sqrt(2F)

    override fun onLayoutChanged(width: Float, height: Float, x: Float, y: Float) {
        super.onLayoutChanged(width, height, x, y)
        measureAndLayout()
    }

    override fun drawBounds(canvas: Canvas) {
        canvas.save()
        canvas.rotate(mRotateDegree, x, y)
        super.drawBounds(canvas)
        canvas.restore()
    }

    override fun addItem(playTime: Long, item: DrawItem<DanmakuData>): Boolean {
        mDrawingItems.maxByOrNull { it.x + it.width }
            ?.takeUnless { hasEnoughSpace(it) }
            ?.let {
                return false
            }
        item.x = x + width * mSqrt2 / 2
        item.y = y - width * mSqrt2 / 2
        item.rotate = mRotateDegree
        item.showTime = playTime
        mDrawingItems.add(item)
        return true
    }

    override fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean): Int {
        if (mLastTypeSettingTime < 0) {
            mLastTypeSettingTime = System.currentTimeMillis()
        } else {
            val newTypeSettingTime = System.currentTimeMillis()
            mStepperTime = if (newTypeSettingTime - mLastTypeSettingTime < HIGH_REFRESH_MAX_TIME) {
                newTypeSettingTime - mLastTypeSettingTime
            } else {
                STEPPER_TIME
            }
            mLastTypeSettingTime = newTypeSettingTime
        }

        if (isPlaying) {
            // move drawing items if is playing
            mDrawingItems.forEach { item ->
                if (!item.isPaused) {
                    val speed = 0.3F
                    item.x -= speed * mStepperTime
                    item.y += speed * mStepperTime
                    item.showDuration += mStepperTime
                }
            }
            // remove items that already out of screen
            val iterator = mDrawingItems.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (item.x + item.width <= x) {
                    mLayer.releaseItem(item)
                    iterator.remove()
                }
            }
        }

        if (configChanged) {
            measureAndLayout()
        }
        return mDrawingItems.size
    }

    private fun hasEnoughSpace(item: DrawItem<DanmakuData>): Boolean {
        val itemMargin = 24f
        return width - (item.x - x) * mSqrt2 - item.width > itemMargin
    }

    private fun measureAndLayout() {
        var lastItem: DrawItem<DanmakuData>? = null
        var lastWidthDiff = 0F
        mDrawingItems.forEach { item ->
            item.y = y
            val oldWidth = item.width
            item.measure(mConfig)
            // if width of the last item increased, check whether there is enough space
            if (lastWidthDiff > 0 && lastItem != null && !hasEnoughSpace(lastItem!!)) {
                item.x += lastWidthDiff
            }
            lastItem = item
            lastWidthDiff = item.width - oldWidth
        }
    }
}
