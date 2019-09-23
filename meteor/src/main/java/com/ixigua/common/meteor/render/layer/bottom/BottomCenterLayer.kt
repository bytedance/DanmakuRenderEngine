package com.ixigua.common.meteor.render.layer.bottom

import android.graphics.Canvas
import android.view.MotionEvent
import com.ixigua.common.meteor.control.ConfigChangeListener
import com.ixigua.common.meteor.control.DanmakuConfig
import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.IRenderLayer
import com.ixigua.common.meteor.render.cache.IDrawCachePool
import com.ixigua.common.meteor.render.cache.LayerBuffer
import com.ixigua.common.meteor.render.draw.DrawItem
import com.ixigua.common.meteor.touch.ITouchDelegate
import com.ixigua.common.meteor.touch.ITouchTarget
import com.ixigua.common.meteor.utils.LAYER_TYPE_BOTTOM_CENTER
import java.util.*

/**
 * Created by dss886 on 2019/9/22.
 */
class BottomCenterLayer(private val mController: DanmakuController,
                        private val mCachePool: IDrawCachePool) : IRenderLayer, ITouchDelegate, ConfigChangeListener {

    /**
     * Line order is reversed: the first line is align the bottom of layer
     */
    private val mLines = LinkedList<BottomCenterLine>()
    private val mPreDrawItems = LinkedList<DrawItem<DanmakuData>>()
    private val mBuffer = LayerBuffer(mController.config, mCachePool)
    private var mWidth = 0
    private var mHeight = 0

    init {
        mController.config.addListener(this)
    }

    override fun getLayerType(): Int {
        return LAYER_TYPE_BOTTOM_CENTER
    }

    override fun onLayoutSizeChanged(width: Int, height: Int) {
        mWidth = width
        mHeight = height
        configLines()
    }

    override fun addItems(playTime: Long, list: List<DrawItem<DanmakuData>>) {
        mBuffer.addItems(list)
        mBuffer.forEach {
            addItemImpl(playTime, it)
        }
        mBuffer.trimBuffer()
    }

    /**
     * Try add item to lines.
     * Return true if find a line to add, return false otherwise.
     */
    private fun addItemImpl(playTime: Long, item: DrawItem<DanmakuData>): Boolean {
        mLines.forEach { line ->
            if (line.isEmpty()) {
                line.addItem(playTime, item)
                return true
            }
        }
        mLines.forEach { line ->
            if (line.addItem(playTime, item)) {
                return true
            }
        }
        return false
    }

    override fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean) {
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
        mLines.asReversed().forEach { line ->
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
            DanmakuConfig.TYPE_BOTTOM_CENTER_LINE_HEIGHT,
            DanmakuConfig.TYPE_BOTTOM_CENTER_LINE_COUNT,
            DanmakuConfig.TYPE_BOTTOM_CENTER_LINE_MARGIN,
            DanmakuConfig.TYPE_BOTTOM_CENTER_MARGIN_BOTTOM -> configLines()
        }
    }

    private fun configLines() {
        val config = mController.config
        val lineCount = config.bottom.lineCount
        val lineHeight = config.bottom.lineHeight
        val lineSpace = config.bottom.lineMargin
        val marginBottom = config.bottom.marginBottom
        if (lineCount > mLines.size) {
            for (i in 1..(lineCount - mLines.size)) {
                mLines.add(0, BottomCenterLine(mController, mCachePool).apply {
                    mController.registerCmdMonitor(this)
                })
            }
        } else if (lineCount < mLines.size) {
            for (i in 1..(mLines.size - lineCount)) {
                mLines.removeAt(0).let {
                    mController.unRegisterCmdMonitor(it)
                }
            }
        }
        mLines.forEachIndexed { index, line ->
            line.onLayoutChanged(mWidth.toFloat(), lineHeight, 0F, mHeight - marginBottom - index * (lineSpace + lineHeight) - lineHeight)
        }
    }

}