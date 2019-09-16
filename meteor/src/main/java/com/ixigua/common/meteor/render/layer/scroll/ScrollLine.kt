package com.ixigua.common.meteor.render.layer.scroll

import android.graphics.*
import android.view.MotionEvent
import com.ixigua.common.meteor.control.DanmakuCommand
import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.control.ICommandMonitor
import com.ixigua.common.meteor.data.IDanmakuData
import com.ixigua.common.meteor.render.cache.IDrawCachePool
import com.ixigua.common.meteor.render.draw.IDrawItem
import com.ixigua.common.meteor.touch.ITouchTarget
import com.ixigua.common.meteor.utils.CMD_PAUSE_ITEM
import com.ixigua.common.meteor.utils.CMD_RESUME_ITEM
import java.util.*

/**
 * Created by dss886 on 2018/11/8.
 */
class ScrollLine(private val mController: DanmakuController,
                 private val mCachePool: IDrawCachePool) : ITouchTarget, ICommandMonitor {

    private val mConfig = mController.config
    private val mDrawingItems = LinkedList<IDrawItem<IDanmakuData>>()
    private var mCurrentTouchItem: IDrawItem<IDanmakuData>? = null
    private var mClickPositionRect = RectF()
    private var mClickPoint = PointF()
    private val mLayoutBoundsPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    var width: Float = 0F
    var height: Float = 0F
    var y: Float = 0F

    fun onLayoutChanged(width: Float, height: Float, y: Float) {
        this.width = width
        this.height = height
        this.y = y
        measureAndLayout()
    }

    fun getPreDrawItems(): List<IDrawItem<IDanmakuData>> {
        return mDrawingItems
    }

    fun addItem(item: IDrawItem<IDanmakuData>): Boolean {
        mDrawingItems.maxBy { it.x + it.width }
                ?.takeUnless { hasEnoughSpace(it, item) }
                ?.let {
                    return false
                }
        item.x = this.width
        item.y = this.y
        mDrawingItems.add(item)
        return true
    }

    /**
     * Do the typesetting work
     * @param isPlaying move item forward if is playing
     * @param configChanged need to re-measure and re-layout items if config changed
     */
    fun typesetting(isPlaying: Boolean, configChanged: Boolean = false) {
        if (isPlaying) {
            // move drawing items if is playing
            mDrawingItems.forEach { item ->
                if (!item.isPaused) {
                    item.x -= getItemSpeed(item) * 16L
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

    fun drawLayoutBounds(canvas: Canvas) {
        mLayoutBoundsPaint.color = Color.argb(50, 0, 255, 0)
        mLayoutBoundsPaint.style = Paint.Style.FILL
        canvas.drawRect(0F, y, width, y + height, mLayoutBoundsPaint)
        mDrawingItems.forEach { item ->
            mLayoutBoundsPaint.color = Color.argb(50, 255, 0, 0)
            canvas.drawRect(item.x, item.y, item.x + item.width, item.y + item.height, mLayoutBoundsPaint)
        }
    }

    fun clearRender() {
        mDrawingItems.clear()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawingItems.asReversed().forEach { item ->
                    if (event.x >= item.x && event.x <= item.x + item.width) {
                        mCurrentTouchItem = item
                        return true
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                mCurrentTouchItem?.let { item ->
                    handleItemClick(item, event.x, event.y)
                    return true
                }
                mCurrentTouchItem = null
            }
        }
        return false
    }

    override fun onCommand(cmd: DanmakuCommand) {
        when (cmd.what) {
            CMD_PAUSE_ITEM -> cmd.data?.let { pauseItem(it) }
            CMD_RESUME_ITEM -> cmd.data?.let { resumeItem(it) }
        }
    }

    private fun pauseItem(data: IDanmakuData) {
        mDrawingItems.forEach { item ->
            if (item.data == data) {
                item.isPaused = true
            }
        }
    }

    private fun resumeItem(data: IDanmakuData) {
        mDrawingItems.forEach { item ->
            if (item.data == data) {
                item.isPaused = false
            }
        }
    }

    private fun handleItemClick(item: IDrawItem<IDanmakuData>, upX: Float, upY: Float) {
        mController.itemClickListener?.let { listener ->
            item.data?.let { danmaku ->
                listener.onDanmakuClick(danmaku, mClickPositionRect.apply {
                    left = item.x
                    top = y
                    right = item.x + item.width
                    bottom = y + height
                }, mClickPoint.apply {
                    x = upX
                    y = upY
                })
            }
        }
    }

    private fun hasEnoughSpace(item: IDrawItem<IDanmakuData>, newItem: IDrawItem<IDanmakuData>): Boolean {
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

    private fun getItemSpeed(item: IDrawItem<IDanmakuData>): Float {
        return (item.width + width) / mConfig.scroll.moveTime
    }

    /**
     * Re-measure and re-layout current drawing items
     */
    private fun measureAndLayout() {
        var lastItem: IDrawItem<IDanmakuData>? = null
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
