package com.ixigua.common.meteor.render

import android.graphics.Canvas
import android.view.MotionEvent
import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.cache.DrawCachePool
import com.ixigua.common.meteor.render.draw.DrawItem
import com.ixigua.common.meteor.render.draw.IDrawItemFactory
import com.ixigua.common.meteor.render.draw.bitmap.BitmapDrawItemFactory
import com.ixigua.common.meteor.render.draw.text.TextDrawItemFactory
import com.ixigua.common.meteor.render.layer.bottom.BottomCenterLayer
import com.ixigua.common.meteor.render.layer.scroll.ScrollLayer
import com.ixigua.common.meteor.render.layer.top.TopCenterLayer
import com.ixigua.common.meteor.touch.ITouchDelegate
import com.ixigua.common.meteor.touch.ITouchTarget
import com.ixigua.common.meteor.utils.LAYER_TYPE_UNDEFINE
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by dss886 on 2018/11/6.
 */
class RenderEngine(private val mController: DanmakuController) : ITouchDelegate {

    private val mRenderLayers = CopyOnWriteArrayList<IRenderLayer>()
    private val mDrawCachePool = DrawCachePool()
    private var mWidth = 0
    private var mHeight = 0
    private var mSaveLayerValue = 0

    init {
        mRenderLayers.add(ScrollLayer(mController, mDrawCachePool))
        mRenderLayers.add(TopCenterLayer(mController, mDrawCachePool))
        mRenderLayers.add(BottomCenterLayer(mController, mDrawCachePool))
        registerDrawItemFactory(TextDrawItemFactory())
        registerDrawItemFactory(BitmapDrawItemFactory())
    }

    fun addRenderLayer(layer: IRenderLayer) {
        if (mRenderLayers.contains(layer)) {
            return
        }
        mRenderLayers.add(layer)
        mRenderLayers.sortBy { it.getLayerZIndex() }
    }

    fun registerDrawItemFactory(factory: IDrawItemFactory) {
        mDrawCachePool.registerFactory(factory)
    }

    fun onLayoutSizeChanged(width: Int, height: Int) {
        mRenderLayers.forEach {
            it.onLayoutSizeChanged(width, height)
        }
        mWidth = width
        mHeight = height
    }

    fun addItems(playTime: Long, items: List<DanmakuData>) {
        mRenderLayers.forEach { layer ->
            items.filter { it.layerType == layer.getLayerType() }.takeIf { it.isNotEmpty() }?.let { list ->
                layer.addItems(playTime, list.map { wrapData(it) })
            }
        }
    }

    fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean = false) {
        mRenderLayers.forEach {
            it.typesetting(playTime, isPlaying, configChanged)
        }
    }

    fun draw(canvas: Canvas) {
        if (mController.config.debug.showLayoutBounds) {
            mRenderLayers.forEach {
                it.drawLayoutBounds(canvas)
            }
        }
        val drawItems = mutableListOf<DrawItem<DanmakuData>>()
        drawItems.clear()
        mRenderLayers.forEach {
            drawItems.addAll(it.getPreDrawItems())
        }
        drawItems.sortBy { it.showTime }
        drawItems.sortBy { it.data?.drawOrder }
        if (mController.config.mask.enable) {
            mSaveLayerValue = canvas.saveLayer(0F, 0F, mWidth.toFloat(), mHeight.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        }

        drawItems.forEach {
            it.draw(canvas, mController.config)
        }

        if (mController.config.mask.enable) {
            canvas.restoreToCount(mSaveLayerValue)
        }
        drawItems.clear()
    }

    override fun findTouchTarget(event: MotionEvent): ITouchTarget? {
        mRenderLayers.asReversed().forEach { layer ->
            (layer as? ITouchDelegate)?.findTouchTarget(event)?.let {
                return it
            }
        }
        return null
    }

    fun clear(layerType: Int = LAYER_TYPE_UNDEFINE, notClearOneself: Boolean = false) {
        if (layerType == LAYER_TYPE_UNDEFINE) {
            for (layer in mRenderLayers) {
                layer.clear(notClearOneself)
            }
        } else {
            mRenderLayers.forEach {
                if (it.getLayerType() == layerType) {
                    it.clear(notClearOneself)
                }
            }
        }
    }

    private fun wrapData(data: DanmakuData): DrawItem<DanmakuData> {
        return mDrawCachePool.acquire(data.drawType).apply {
            bindData(data)
            measure(mController.config)
        }
    }

}