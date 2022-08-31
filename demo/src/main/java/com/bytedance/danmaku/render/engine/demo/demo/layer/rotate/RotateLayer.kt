package com.bytedance.danmaku.render.engine.demo.demo.layer.rotate

import android.graphics.Canvas
import android.view.MotionEvent
import com.bytedance.danmaku.render.engine.control.ConfigChangeListener
import com.bytedance.danmaku.render.engine.control.DanmakuConfig
import com.bytedance.danmaku.render.engine.control.DanmakuController
import com.bytedance.danmaku.render.engine.control.Events
import com.bytedance.danmaku.render.engine.data.DanmakuData
import com.bytedance.danmaku.render.engine.render.IRenderLayer
import com.bytedance.danmaku.render.engine.render.cache.IDrawCachePool
import com.bytedance.danmaku.render.engine.render.cache.LayerBuffer
import com.bytedance.danmaku.render.engine.render.draw.DrawItem
import com.bytedance.danmaku.render.engine.touch.ITouchDelegate
import com.bytedance.danmaku.render.engine.touch.ITouchTarget
import com.bytedance.danmaku.render.engine.utils.EVENT_DANMAKU_DISMISS
import com.bytedance.danmaku.render.engine.utils.EVENT_DANMAKU_SHOW
import com.bytedance.danmaku.render.engine.demo.utils.LAYER_TYPE_ROTATE
import com.bytedance.danmaku.render.engine.demo.utils.LAYER_Z_INDEX_ROTATE
import java.util.*
import kotlin.math.min

/**
 * Created by dss886 on 2021/07/08.
 */
class RotateLayer: IRenderLayer, ITouchDelegate, ConfigChangeListener {

    private lateinit var mController: DanmakuController
    private lateinit var mCachePool: IDrawCachePool
    private lateinit var mBuffer: LayerBuffer
    private lateinit var mConfig: DanmakuConfig
    private val mLines = LinkedList<RotateLine>()
    private val mPreDrawItems = LinkedList<DrawItem<DanmakuData>>()
    private val mLineCount = 8
    private val mLineHeight = 54f
    private var mTotalDanmakuCountInLayer = 0
    private var mWidth = 0
    private var mHeight = 0

    override fun init(controller: DanmakuController, cachePool: IDrawCachePool) {
        mController = controller
        mCachePool = cachePool
        mConfig = mController.config
        mBuffer = LayerBuffer(mConfig, mCachePool, 4, 2000L)
        mConfig.addListener(this)
    }

    override fun getLayerType(): Int {
        return LAYER_TYPE_ROTATE
    }

    override fun getLayerZIndex(): Int {
        return LAYER_Z_INDEX_ROTATE
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
        // Not allowed to be touched
        return null
    }

    override fun onConfigChanged(type: Int) {
        // Nothing to do
    }

    /**
     * Try add item to lines.
     * Return true if find a line to add, return false otherwise.
     */
    private fun distributeItemToLines(playTime: Long, item: DrawItem<DanmakuData>): Boolean {
        mLines.maxByOrNull { it.getCurrentItemShowDuration() }?.let { line ->
            if (line.addItem(playTime, item)) {
                mController.notifyEvent(Events.obtainEvent(EVENT_DANMAKU_SHOW, item.data))
                return true
            }
        }
        return false
    }

    private fun configLines() {
        val size = min(mWidth, mHeight).toFloat()
        if (mLines.isEmpty()) {
            for (i in 1..mLineCount) {
                mLines.add(RotateLine(mController, this).apply {
                    mController.registerCmdMonitor(this)
                })
            }
        }
        val degreePerLine = 90F / mLineCount
        mLines.forEachIndexed { index, line ->
            line.onLayoutChanged(size, mLineHeight, 0F, 0F)
            line.rotateDegree = degreePerLine * index
        }
    }

}