package com.ixigua.common.meteor.render.layer.scroll

import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.cache.IDrawCachePool
import com.ixigua.common.meteor.render.draw.DrawItem
import com.ixigua.common.meteor.render.layer.line.BaseRenderLine
import com.ixigua.common.meteor.utils.STEPPER_TIME

/**
 * Created by dss886 on 2018/11/8.
 */
class ScrollLine(mController: DanmakuController,
                 private val mCachePool: IDrawCachePool) : BaseRenderLine(mController) {

    override fun onLayoutChanged(width: Float, height: Float, x: Float, y: Float) {
        super.onLayoutChanged(width, height, x, y)
        measureAndLayout()
    }

    override fun addItem(playTime: Long, item: DrawItem<DanmakuData>): Boolean {
        mDrawingItems.maxBy { it.x + it.width }
                ?.takeUnless { hasEnoughSpace(it, item) }
                ?.let {
                    return false
                }
        item.x = this.width
        item.y = this.y
        item.showTime = playTime
        mDrawingItems.add(item)
        return true
    }

    /**
     * Do the typesetting work
     * @param isPlaying move item forward if is playing
     * @param configChanged need to re-measure and re-layout items if config changed
     */
    override fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean) {
        if (isPlaying) {
            // move drawing items if is playing
            mDrawingItems.forEach { item ->
                if (!item.isPaused) {
                    item.x -= getItemSpeed(item) * STEPPER_TIME
                    item.showDuration += STEPPER_TIME
                }
            }
            // remove items that already out of screen
            val iterator = mDrawingItems.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (item.x + item.width <= 0) {
                    mCachePool.release(item)
                    iterator.remove()
                }
            }
        }
        if (configChanged) {
            measureAndLayout()
        }
    }

    private fun hasEnoughSpace(item: DrawItem<DanmakuData>, newItem: DrawItem<DanmakuData>): Boolean {
        val nowSpace = width - item.x - item.width
        if (nowSpace < mConfig.scroll.itemMargin) {
            return false
        }
        val speed = getItemSpeed(item)
        val newSpeed = getItemSpeed(newItem)
        return if (speed >= newSpeed) true else {
            nowSpace - (newSpeed - speed) * mConfig.scroll.moveTime >= mConfig.scroll.itemMargin
        }
    }

    private fun getItemSpeed(item: DrawItem<DanmakuData>): Float {
        return (item.width + width) / mConfig.scroll.moveTime
    }

    /**
     * Re-measure and re-layout current drawing items
     */
    private fun measureAndLayout() {
        var lastItem: DrawItem<DanmakuData>? = null
        var lastWidthDiff = 0F
        mDrawingItems.forEach { item ->
            item.y = y
            val oldWidth = item.width
            item.measure(mConfig)
            // if width of the last item increased, check whether there is enough space
            if (lastWidthDiff > 0 && lastItem != null && !hasEnoughSpace(lastItem!!, item)) {
                item.x += lastWidthDiff
            }
            lastItem = item
            lastWidthDiff = item.width - oldWidth
        }
    }
}
