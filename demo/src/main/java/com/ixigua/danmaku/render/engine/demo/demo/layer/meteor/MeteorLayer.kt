package com.ixigua.danmaku.render.engine.demo.demo.layer.meteor

import android.graphics.Canvas
import android.view.MotionEvent
import com.ixigua.common.danmaku.render.engine.control.ConfigChangeListener
import com.ixigua.common.danmaku.render.engine.control.DanmakuConfig
import com.ixigua.common.danmaku.render.engine.control.DanmakuController
import com.ixigua.common.danmaku.render.engine.control.Events
import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.IRenderLayer
import com.ixigua.common.danmaku.render.engine.render.cache.IDrawCachePool
import com.ixigua.common.danmaku.render.engine.render.cache.LayerBuffer
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem
import com.ixigua.common.danmaku.render.engine.touch.ITouchDelegate
import com.ixigua.common.danmaku.render.engine.touch.ITouchTarget
import com.ixigua.common.danmaku.render.engine.utils.EVENT_DANMAKU_DISMISS
import com.ixigua.common.danmaku.render.engine.utils.EVENT_DANMAKU_SHOW
import com.ixigua.danmaku.render.engine.demo.utils.LAYER_TYPE_METEOR
import com.ixigua.danmaku.render.engine.demo.utils.LAYER_Z_INDEX_METEOR
import java.util.*
import kotlin.math.sqrt

/**
 * Created by dss886 on 2021/08/19.
 * This class is for demonstration only, as many details are not implemented.
 */
class MeteorLayer : IRenderLayer, ITouchDelegate, ConfigChangeListener {

    private lateinit var mController: DanmakuController
    private lateinit var mCachePool: IDrawCachePool
    private lateinit var mBuffer: LayerBuffer
    private lateinit var mConfig: DanmakuConfig
    private val mLines = LinkedList<MeteorLine>()
    private val mPreDrawItems = LinkedList<DrawItem<DanmakuData>>()
    private var mTotalDanmakuCountInLayer = 0
    private var mWidth = 0
    private var mHeight = 0

    override fun init(controller: DanmakuController, cachePool: IDrawCachePool) {
        mController = controller
        mCachePool = cachePool
        mConfig = mController.config
        mBuffer = LayerBuffer(mConfig, mCachePool, mConfig.scroll.bufferSize, mConfig.scroll.bufferMaxTime)
        mConfig.addListener(this)
    }

    override fun getLayerType(): Int {
        return LAYER_TYPE_METEOR
    }

    override fun getLayerZIndex(): Int {
        return LAYER_Z_INDEX_METEOR
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
        mController.notifyEvent(Events.obtainEvent(EVENT_DANMAKU_DISMISS, item.data))
        mCachePool.release(item)
    }

    override fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean): Int {
        mBuffer.forEach {
            distributeItemToLines(playTime, it)
        }
        mTotalDanmakuCountInLayer = 0
        mLines.forEach { line ->
            mTotalDanmakuCountInLayer += line.typesetting(playTime, isPlaying, configChanged)
        }
        if (configChanged) {
            mBuffer.measureItems()
        }
        return mTotalDanmakuCountInLayer
    }

    override fun drawBounds(canvas: Canvas) {
        mLines.forEach { line ->
            line.drawBounds(canvas)
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
        // do nothing
        return null
    }

    override fun onConfigChanged(type: Int) {
        // do nothing
    }

    private fun distributeItemToLines(playTime: Long, item: DrawItem<DanmakuData>): Boolean {
        mLines.forEach { line ->
            if (line.addItem(playTime, item)) {
                mController.notifyEvent(Events.obtainEvent(EVENT_DANMAKU_SHOW, item.data))
                return true
            }
        }
        return false
    }

    private fun configLines() {
        val lineCount = 6
        val lineHeight = 54F
        val lineSpace = 120F
        val marginTop = 120F
        val sqrt2 = sqrt(2F)
        if (lineCount > mLines.size) {
            for (i in 1..(lineCount - mLines.size)) {
                mLines.add(MeteorLine(mController, this).apply {
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
            val distance = marginTop + index * (lineSpace + lineHeight)
            val x: Float
            val y: Float
            val width: Float
            when {
                distance < mHeight * sqrt2 / 2 -> {
                    x = 0F
                    y = distance * sqrt2
                    width = 2 * distance
                }
                distance < mWidth * sqrt2 / 2 -> {
                    x = distance * sqrt2 - mHeight.toFloat()
                    y = mHeight.toFloat()
                    width = mHeight.toFloat() * sqrt2
                }
                distance < mHeight * sqrt2 -> {
                    x = distance * sqrt2 - mHeight.toFloat()
                    y = mHeight.toFloat()
                    width = (mWidth - x) * sqrt2
                }
                else -> {
                    x = distance * sqrt2 - mHeight
                    y = mHeight.toFloat()
                    width = (mWidth - x) * sqrt2
                }
            }
            line.onLayoutChanged(width, lineHeight, x, y)
        }
    }

}
