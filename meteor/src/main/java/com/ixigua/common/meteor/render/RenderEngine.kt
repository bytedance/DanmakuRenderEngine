package com.ixigua.common.meteor.render

import android.graphics.Canvas
import android.view.MotionEvent
import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.data.IDanmakuData
import com.ixigua.common.meteor.render.cache.DrawCachePool
import com.ixigua.common.meteor.render.draw.IDrawItem
import com.ixigua.common.meteor.render.draw.IDrawItemFactory
import com.ixigua.common.meteor.render.draw.bitmap.BitmapDrawItemFactory
import com.ixigua.common.meteor.render.draw.text.TextDrawItemFactory
import com.ixigua.common.meteor.render.layer.scroll.ScrollLayer
import com.ixigua.common.meteor.touch.ITouchDelegate
import com.ixigua.common.meteor.touch.ITouchTarget

/**
 * Created by dss886 on 2018/11/6.
 */
class RenderEngine(private val mController: DanmakuController): ITouchDelegate {

    private val mRenderLayers = mutableListOf<IRenderLayer>()
    private val mPreDrawItems = mutableListOf<IDrawItem<IDanmakuData>>()
    private val mDrawCachePool = DrawCachePool()

    init {
        addLayers()
        registerDrawItemFactory(TextDrawItemFactory())
        registerDrawItemFactory(BitmapDrawItemFactory())
    }

    /**
     * add layers by z-index (asc)
     */
    private fun addLayers() {
        mRenderLayers.add(ScrollLayer(mController, mDrawCachePool))
    }

    fun registerDrawItemFactory(factory: IDrawItemFactory) {
        mDrawCachePool.registerFactory(factory)
    }

    fun onLayoutSizeChanged(width: Int, height: Int) {
        mRenderLayers.forEach {
            it.onLayoutSizeChanged(width, height)
        }
    }

    fun addItems(items: List<IDanmakuData>) {
        mRenderLayers.forEach { layer ->
            items.filter { it.layerType == layer.getLayerType() }.takeIf { it.isNotEmpty() }?.let { list ->
                layer.addItems(list.map { wrapData(it) })
            }
        }
    }

    fun typesetting(isPlaying: Boolean, configChanged: Boolean = false) {
        mRenderLayers.forEach {
            it.typesetting(isPlaying, configChanged)
        }
    }

    fun draw(canvas: Canvas) {
        if (mController.config.debug.showLayoutBounds) {
            mRenderLayers.forEach {
                it.drawLayoutBounds(canvas)
            }
        }
        mPreDrawItems.clear()
        mRenderLayers.forEach {
            mPreDrawItems.addAll(it.getPreDrawItems())
        }
        mPreDrawItems.sortBy { it.data?.showAtTime }
        mPreDrawItems.sortBy { it.data?.drawOrder }
        mPreDrawItems.forEach {
            it.draw(canvas, mController.config)
        }
        mPreDrawItems.clear()
    }

    override fun findTouchTarget(event: MotionEvent): ITouchTarget? {
        mRenderLayers.asReversed().forEach { layer ->
            (layer as? ITouchDelegate)?.findTouchTarget(event)?.let {
                return it
            }
        }
        return null
    }

    fun clear() {
        for (layer in mRenderLayers) {
            layer.clear()
        }
    }

    private fun wrapData(data: IDanmakuData): IDrawItem<IDanmakuData> {
        return mDrawCachePool.acquire(data.drawType).apply {
            bindData(data)
            measure(mController.config)
        }
    }

}