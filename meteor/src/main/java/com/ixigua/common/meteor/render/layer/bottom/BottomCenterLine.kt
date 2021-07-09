package com.ixigua.common.meteor.render.layer.bottom

import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.IRenderLayer
import com.ixigua.common.meteor.render.draw.DrawItem
import com.ixigua.common.meteor.render.layer.line.BaseRenderLine
import com.ixigua.common.meteor.utils.STEPPER_TIME

/**
 * Created by dss886 on 2019/9/22.
 */
class BottomCenterLine(controller: DanmakuController,
                       private val mLayer: IRenderLayer) : BaseRenderLine(controller, mLayer) {

    override fun onLayoutChanged(width: Float, height: Float, x: Float, y: Float) {
        super.onLayoutChanged(width, height, x, y)
        measureAndLayout()
    }

    override fun addItem(playTime: Long, item: DrawItem<DanmakuData>): Boolean {
        mDrawingItems.takeIf { it.isNotEmpty() }?.get(0)?.let {
            if (it.isPaused) {
                return false
            }
            if (it.showDuration < mConfig.bottom.showTimeMin) {
                return false
            }
            mDrawingItems.clear()
            mLayer.releaseItem(it)
        }
        item.x = (width - item.width) / 2
        item.y = this.y
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
            if (item.showDuration >= mConfig.bottom.showTimeMax) {
                mLayer.releaseItem(item)
                mDrawingItems.clear()
            }
        }
        if (configChanged) {
            measureAndLayout()
        }
        return mDrawingItems.size
    }

    /**
     * Re-measure and re-layout current drawing items
     */
    private fun measureAndLayout() {
        mDrawingItems.forEach { item ->
            item.measure(mConfig)
            item.y = y
            item.x = (width - item.width) / 2
        }
    }

}