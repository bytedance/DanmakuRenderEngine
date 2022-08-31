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
import com.bytedance.danmaku.render.engine.control.DanmakuController
import com.bytedance.danmaku.render.engine.data.DanmakuData
import com.bytedance.danmaku.render.engine.render.cache.IDrawCachePool
import com.bytedance.danmaku.render.engine.render.draw.DrawItem

/**
 * Created by dss886 on 2018/11/8.
 */
interface IRenderLayer {

    fun init(controller: DanmakuController, cachePool: IDrawCachePool)

    fun getLayerType(): Int

    fun getLayerZIndex(): Int

    /**
     * Calculate the width and height in this callback and rearrange your RenderLines (if has).
     */
    fun onLayoutSizeChanged(width: Int, height: Int)

    /**
     * Items has been measured already.
     */
    fun addItems(playTime: Long, list: List<DrawItem<DanmakuData>>)

    fun releaseItem(item: DrawItem<DanmakuData>)

    fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean = false): Int

    fun drawBounds(canvas: Canvas)

    fun getPreDrawItems(): List<DrawItem<DanmakuData>>

    fun clear()

}
