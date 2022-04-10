package com.ixigua.danmaku.render.engine.demo.demo.layer.rotate

import android.graphics.Canvas
import com.ixigua.common.danmaku.render.engine.control.DanmakuController
import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.IRenderLayer
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem
import com.ixigua.common.danmaku.render.engine.render.layer.line.BaseRenderLine
import com.ixigua.common.danmaku.render.engine.utils.STEPPER_TIME

/**
 * Created by dss886 on 2021/07/08.
 */
class RotateLine(controller: DanmakuController,
                 private val mLayer: IRenderLayer) : BaseRenderLine(controller, mLayer) {

    var rotateDegree = 0f

    private val mShowTime = 5000L

    override fun onLayoutChanged(width: Float, height: Float, x: Float, y: Float) {
        super.onLayoutChanged(width, height, x, y)
        measureAndLayout()
    }

    override fun drawBounds(canvas: Canvas) {
        canvas.save()
        canvas.rotate(rotateDegree, x, y)
        super.drawBounds(canvas)
        canvas.restore()
    }

    override fun addItem(playTime: Long, item: DrawItem<DanmakuData>): Boolean {
        mDrawingItems.takeIf { it.isNotEmpty() }?.get(0)?.let {
            return false
        }
        item.x = this.x
        item.y = this.y
        item.rotate = rotateDegree
        item.showTime = playTime
        mDrawingItems.clear()
        mDrawingItems.add(item)
        return true
    }

    /**
     * Return the duration the item has been showed. The longer the duration, the easier it is
     * to be replaced by new item. If there is no item showing, return the [Long.MAX_VALUE]
     */
    fun getCurrentItemShowDuration(): Long {
        return mDrawingItems.takeIf { it.isNotEmpty() }?.get(0)?.showDuration ?: Long.MAX_VALUE
    }

    override fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean): Int {
        if (isPlaying) {
            val item = mDrawingItems.takeIf { it.isNotEmpty() }?.get(0) ?: return 0
            if (!item.isPaused) {
                item.showDuration += STEPPER_TIME
            }
            // remove items that has shown enough time
            if (item.showDuration >= mShowTime) {
                mLayer.releaseItem(item)
                mDrawingItems.clear()
            }
        }
        if (configChanged) {
            measureAndLayout()
        }
        return mDrawingItems.size
    }

    private fun measureAndLayout() {
        mDrawingItems.forEach { item ->
            item.measure(mConfig)
            item.x = x
            item.y = y
        }
    }

}