/*
 * Copyright (C) 2022 ByteDance Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bytedance.danmaku.render.engine.render

import android.graphics.Canvas
import android.view.MotionEvent
import com.bytedance.danmaku.render.engine.control.DanmakuController
import com.bytedance.danmaku.render.engine.data.DanmakuData
import com.bytedance.danmaku.render.engine.render.cache.DrawCachePool
import com.bytedance.danmaku.render.engine.render.draw.DrawItem
import com.bytedance.danmaku.render.engine.render.draw.IDrawItemFactory
import com.bytedance.danmaku.render.engine.render.draw.bitmap.BitmapDrawItemFactory
import com.bytedance.danmaku.render.engine.render.draw.mask.MaskDanmakuFactory
import com.bytedance.danmaku.render.engine.render.draw.text.TextDrawItemFactory
import com.bytedance.danmaku.render.engine.render.layer.bottom.BottomCenterLayer
import com.bytedance.danmaku.render.engine.render.layer.mask.MaskLayer
import com.bytedance.danmaku.render.engine.render.layer.scroll.ScrollLayer
import com.bytedance.danmaku.render.engine.render.layer.top.TopCenterLayer
import com.bytedance.danmaku.render.engine.touch.ITouchDelegate
import com.bytedance.danmaku.render.engine.touch.ITouchTarget
import com.bytedance.danmaku.render.engine.utils.LAYER_TYPE_UNDEFINE
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

    private var onDrawingDanmakuCount = 0

    init {
        mRenderLayers.add(ScrollLayer().apply { init(mController, mDrawCachePool) })
        mRenderLayers.add(TopCenterLayer().apply { init(mController, mDrawCachePool) })
        mRenderLayers.add(BottomCenterLayer().apply { init(mController, mDrawCachePool) })
        mRenderLayers.add(MaskLayer().apply { init(mController, mDrawCachePool) })
        registerDrawItemFactory(TextDrawItemFactory())
        registerDrawItemFactory(BitmapDrawItemFactory())
        registerDrawItemFactory(MaskDanmakuFactory())
    }

    fun addRenderLayer(layer: IRenderLayer) {
        if (mRenderLayers.contains(layer)) {
            return
        }
        mRenderLayers.add(layer.apply { init(mController, mDrawCachePool) })
        sortRenderLayers()
    }

    private fun sortRenderLayers() {
        val renderLayers = mutableListOf<IRenderLayer>()
        renderLayers.addAll(mRenderLayers)
        renderLayers.sortBy { it.getLayerZIndex() }
        mRenderLayers.clear()
        mRenderLayers.addAll(renderLayers)
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
                layer.addItems(playTime, list.map { wrapData(layer, it) })
            }
        }
    }

    fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean = false): Int {
        onDrawingDanmakuCount = 0
        mRenderLayers.forEach {
            onDrawingDanmakuCount += it.typesetting(playTime, isPlaying, configChanged)
        }
        return onDrawingDanmakuCount
    }

    fun draw(canvas: Canvas) {
        if (mController.config.debug.showBounds) {
            mRenderLayers.forEach {
                it.drawBounds(canvas)
            }
        }
        val drawItems = mutableListOf<DrawItem<DanmakuData>>()
        drawItems.clear()
        mRenderLayers.forEach {
            drawItems.addAll(it.getPreDrawItems())
        }

        drawItems.sortWith(compareBy(
            { it.data?.drawOrder },
            { it.layerZIndex },
            { it.showTime }
        ))

        if (mController.config.mask.enable) {
            @Suppress("DEPRECATION")
            mSaveLayerValue = canvas.saveLayer(0F, 0F, mWidth.toFloat(), mHeight.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        }

        drawItems.forEach {
            it.draw(canvas, mController.config)
            if (mController.config.debug.showBounds) {
                it.drawBounds(canvas)
            }
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

    fun clear(layerType: Int = LAYER_TYPE_UNDEFINE) {
        if (layerType == LAYER_TYPE_UNDEFINE) {
            for (layer in mRenderLayers) {
                layer.clear()
            }
        } else {
            mRenderLayers.forEach {
                if (it.getLayerType() == layerType) {
                    it.clear()
                }
            }
        }
    }

    private fun wrapData(layer: IRenderLayer, data: DanmakuData): DrawItem<DanmakuData> {
        return mDrawCachePool.acquire(data.drawType).apply {
            layerZIndex = layer.getLayerZIndex()
            bindData(data)
            measure(mController.config)
        }
    }

}