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
package com.ixigua.common.danmaku.render.engine.render.layer.top

import com.ixigua.common.danmaku.render.engine.control.DanmakuController
import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.IRenderLayer
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem
import com.ixigua.common.danmaku.render.engine.render.layer.line.BaseRenderLine
import com.ixigua.common.danmaku.render.engine.utils.STEPPER_TIME

/**
 * Created by dss886 on 2019/9/22.
 */
class TopCenterLine(controller: DanmakuController,
                    private val mLayer: IRenderLayer) : BaseRenderLine(controller, mLayer) {

    override fun onLayoutChanged(width: Float, height: Float, x: Float, y: Float) {
        super.onLayoutChanged(width, height, x, y)
        measureAndLayout()
    }

    override fun addItem(playTime: Long, item: DrawItem<DanmakuData>): Boolean {
        mDrawingItems.takeIf { it.isNotEmpty() }?.get(0)?.let {
            if (it.isPaused) {
                return false
            }
            if (it.showDuration < mConfig.top.showTimeMin) {
                return false
            }
            mDrawingItems.clear()
            mLayer.releaseItem(it)
        }
        item.x = (width - item.width) / 2
        item.y = this.y
        item.showTime = playTime
        mDrawingItems.clear()
        mDrawingItems.add(item)
        return true
    }

    /**
     * Return the duration the item has been showed. The longer the duration, the easier it is
     * to be replaced by new item. If there is no item showing, return the [Long.MAX_VALUE]
     */
    fun getCurrentItemShowDuration(): Long {
        return mDrawingItems.takeIf { it.isNotEmpty() }?.get(0)?.showDuration ?: Long.MAX_VALUE
    }

    override fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean): Int {
        if (isPlaying) {
            val item = mDrawingItems.takeIf { it.isNotEmpty() }?.get(0) ?: return 0
            if (!item.isPaused) {
                item.showDuration += STEPPER_TIME
            }
            // remove items that has shown enough time
            if (item.showDuration >= mConfig.top.showTimeMax) {
                mLayer.releaseItem(item)
                mDrawingItems.clear()
            }
        }
        if (configChanged) {
            measureAndLayout()
        }
        return mDrawingItems.size
    }

    /**
     * Re-measure and re-layout current drawing items
     */
    private fun measureAndLayout() {
        mDrawingItems.forEach { item ->
            item.measure(mConfig)
            item.y = y
            item.x = (width - item.width) / 2
        }
    }

}