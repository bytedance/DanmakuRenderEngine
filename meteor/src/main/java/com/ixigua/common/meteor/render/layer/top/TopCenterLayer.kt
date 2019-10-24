package com.ixigua.common.meteor.render.layer.top

import android.graphics.Canvas
import android.view.MotionEvent
import com.ixigua.common.meteor.control.ConfigChangeListener
import com.ixigua.common.meteor.control.DanmakuConfig
import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.control.Events
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.IRenderLayer
import com.ixigua.common.meteor.render.cache.IDrawCachePool
import com.ixigua.common.meteor.render.cache.LayerBuffer
import com.ixigua.common.meteor.render.draw.DrawItem
import com.ixigua.common.meteor.touch.ITouchDelegate
import com.ixigua.common.meteor.touch.ITouchTarget
import com.ixigua.common.meteor.utils.EVENT_DANMAKU_DISMISS
import com.ixigua.common.meteor.utils.EVENT_DANMAKU_SHOW
import com.ixigua.common.meteor.utils.LAYER_TYPE_TOP_CENTER
import com.ixigua.common.meteor.utils.LAYER_Z_INDEX_TOP_CENTER
import java.util.*

/**
 * Created by dss886 on 2019/9/23.
 */
class TopCenterLayer(private val mController: DanmakuController,
                     private val mCachePool: IDrawCachePool) : IRenderLayer, ITouchDelegate, ConfigChangeListener {

    private val mLines = LinkedList<TopCenterLine>()
    private val mPreDrawItems = LinkedList<DrawItem<DanmakuData>>()
    private val mConfig = mController.config
    private val mBuffer = LayerBuffer(mConfig, mCachePool, mConfig.top.bufferSize, mConfig.top.bufferMaxTime)
    private var mWidth = 0
    private var mHeight = 0

    init {
        mConfig.addListener(this)
    }

    override fun getLayerType(): Int {
        return LAYER_TYPE_TOP_CENTER
    }

    override fun getLayerZIndex(): Int {
        return LAYER_Z_INDEX_TOP_CENTER
    }

    override fun onLayoutSizeChanged(width: Int, height: Int) {
        mWidth = width
        mHeight = height
        configLines()
    }

    override fun addItems(playTime: Long, list: List<DrawItem<DanmakuData>>) {
        mBuffer.addItems(list)
        mBuffer.trimBuffer(playTime)
    }

    override fun releaseItem(item: DrawItem<DanmakuData>) {
        mController.notifyEvent(Events.obtainEvent( EVENT_DANMAKU_DISMISS, item.data))
        mCachePool.release(item)
    }

    override fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean) {
        mBuffer.forEach {
            addItemImpl(playTime, it)
        }
        mLines.forEach { line ->
            line.typesetting(playTime, isPlaying, configChanged)
        }
        if (configChanged) {
            mBuffer.measureItems()
        }
    }

    override fun drawLayoutBounds(canvas: Canvas) {
        mLines.forEach { line ->
            line.drawLayoutBounds(canvas)
        }
    }

    override fun getPreDrawItems(): List<DrawItem<DanmakuData>> {
        mPreDrawItems.clear()
        mLines.forEach { line ->
            mPreDrawItems.addAll(line.getPreDrawItems())
        }
        return mPreDrawItems
    }

    override fun clear() {
        mLines.forEach { line ->
            line.clearRender()
        }
        mBuffer.clear()
    }

    override fun findTouchTarget(event: MotionEvent): ITouchTarget? {
        mLines.forEach { line ->
            if (event.y > line.y + line.height) {
                return@forEach
            }
            if (event.y >= line.y && line.onTouchEvent(event)) {
                return line
            }
            return null
        }
        return null
    }

    override fun onConfigChanged(type: Int) {
        when (type) {
            DanmakuConfig.TYPE_TOP_CENTER_LINE_HEIGHT,
            DanmakuConfig.TYPE_TOP_CENTER_LINE_COUNT,
            DanmakuConfig.TYPE_TOP_CENTER_LINE_MARGIN,
            DanmakuConfig.TYPE_TOP_CENTER_MARGIN_TOP -> configLines()
            DanmakuConfig.TYPE_TOP_CENTER_BUFFER_MAX_TIME,
            DanmakuConfig.TYPE_TOP_CENTER_BUFFER_SIZE -> {
                mBuffer.onBufferChanged(mConfig.top.bufferSize, mConfig.top.bufferMaxTime)
            }
        }
    }

    /**
     * Try add item to lines.
     * Return true if find a line to add, return false otherwise.
     */
    private fun addItemImpl(playTime: Long, item: DrawItem<DanmakuData>): Boolean {
        mLines.maxBy { it.getCurrentItemShowDuration() ?: Long.MAX_VALUE }?.let { line ->
            if (line.addItem(playTime, item)) {
                mController.notifyEvent(Events.obtainEvent(EVENT_DANMAKU_SHOW, item.data))
                return true
            }
        }
        return false
    }

    private fun configLines() {
        val lineCount = mConfig.top.lineCount
        val lineHeight = mConfig.top.lineHeight
        val lineSpace = mConfig.top.lineMargin
        val marginTop = mConfig.top.marginTop
        if (lineCount > mLines.size) {
            for (i in 1..(lineCount - mLines.size)) {
                mLines.add(TopCenterLine(mController, this).apply {
                    mController.registerCmdMonitor(this)
                })
            }
        } else if (lineCount < mLines.size) {
            for (i in 1..(mLines.size - lineCount)) {
                mLines.removeAt(mLines.size - 1).let {
                    mController.unRegisterCmdMonitor(it)
                }
            }
        }
        mLines.forEachIndexed { index, line ->
            line.onLayoutChanged(mWidth.toFloat(), lineHeight, 0F, marginTop + index * (lineSpace + lineHeight))
        }
    }

}