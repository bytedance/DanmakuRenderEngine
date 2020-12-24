package com.ixigua.common.meteor.render.layer.line

import android.graphics.*
import android.view.MotionEvent
import com.ixigua.common.meteor.control.DanmakuCommand
import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.control.Events
import com.ixigua.common.meteor.control.ICommandMonitor
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.IRenderLayer
import com.ixigua.common.meteor.render.draw.DrawItem
import com.ixigua.common.meteor.touch.ITouchTarget
import com.ixigua.common.meteor.utils.CMD_PAUSE_ITEM
import com.ixigua.common.meteor.utils.CMD_REMEASURE_ITEM
import com.ixigua.common.meteor.utils.CMD_RESUME_ITEM
import com.ixigua.common.meteor.utils.EVENT_DANMAKU_REMEASURE
import java.util.*

/**
 * Created by dss886 on 2019/9/22.
 */
abstract class BaseRenderLine(private val mController: DanmakuController,
                              private val mLayer: IRenderLayer): IRenderLine, ITouchTarget, ICommandMonitor {

    protected val mConfig = mController.config
    protected val mDrawingItems = LinkedList<DrawItem<DanmakuData>>()

    private val mLayoutBoundsPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private var mCurrentTouchItem: DrawItem<DanmakuData>? = null
    private var mClickPositionRect = RectF()
    private var mClickPoint = PointF()

    var width: Float = 0F
    var height: Float = 0F
    var x: Float = 0F
    var y: Float = 0F

    override fun onLayoutChanged(width: Float, height: Float, x: Float, y: Float) {
        this.width = width
        this.height = height
        this.x = x
        this.y = y
    }

    override fun clearRender(notClearOneself : Boolean) {
        if (!notClearOneself) {
            mDrawingItems.forEach {
                mLayer.releaseItem(it)
            }
            mDrawingItems.clear()
        } else {
            val iterator = mDrawingItems.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next.data?.isOneselfSend == false) {
                    mLayer.releaseItem(next)
                    iterator.remove()
                }
            }
        }
    }

    override fun getPreDrawItems(): List<DrawItem<DanmakuData>> {
        return mDrawingItems
    }

    override fun drawLayoutBounds(canvas: Canvas) {
        mLayoutBoundsPaint.color = Color.argb(50, 0, 255, 0)
        mLayoutBoundsPaint.style = Paint.Style.FILL
        canvas.drawRect(0F, y, width, y + height, mLayoutBoundsPaint)
        mDrawingItems.forEach { item ->
            mLayoutBoundsPaint.color = Color.argb(100, 255, 0, 0)
            canvas.drawRect(item.x, item.y, item.x + item.width, item.y + item.height, mLayoutBoundsPaint)
        }
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
                    if (touchValidate(event, item)) {
                        handleItemClick(item, event.x, event.y)
                    }
                    return true
                }
                mCurrentTouchItem = null
            }
        }
        return false
    }

    /**
     * Do not handle click if touch event is out of item area
     */
    private fun touchValidate(event: MotionEvent, item: DrawItem<DanmakuData>): Boolean {
        return event.x in item.x..item.x + item.width
    }

    open fun handleItemClick(item: DrawItem<DanmakuData>, upX: Float, upY: Float) {
        mController.itemClickListener?.let { listener ->
            item.data?.let { danmaku ->
                listener.onDanmakuClick(danmaku, mClickPositionRect.apply {
                    left = item.x
                    top = item.y
                    right = item.x + item.width
                    bottom = item.y + item.height
                }, mClickPoint.apply {
                    x = upX
                    y = upY
                })
            }
        }
    }

    override fun onCommand(cmd: DanmakuCommand) {
        when (cmd.what) {
            CMD_PAUSE_ITEM -> cmd.data?.let { pauseItem(it) }
            CMD_RESUME_ITEM -> cmd.data?.let { resumeItem(it) }
            CMD_REMEASURE_ITEM -> cmd.data?.let { measureItem(it) }
        }
    }

    private fun pauseItem(data: DanmakuData) {
        mDrawingItems.forEach { item ->
            if (item.data == data) {
                item.isPaused = true
            }
        }
    }

    private fun resumeItem(data: DanmakuData) {
        mDrawingItems.forEach { item ->
            if (item.data == data) {
                item.isPaused = false
            }
        }
    }

    private fun measureItem(data: DanmakuData) {
        mDrawingItems.forEach { item ->
            if (item.data == data) {
                item.bindData(data)
                item.measure(mConfig)
                mController.notifyEvent(Events.obtainEvent(EVENT_DANMAKU_REMEASURE, data, mClickPositionRect.apply {
                    left = item.x
                    top = item.y
                    right = item.x + item.width
                    bottom = item.y + item.height
                }))
            }
        }
    }

}