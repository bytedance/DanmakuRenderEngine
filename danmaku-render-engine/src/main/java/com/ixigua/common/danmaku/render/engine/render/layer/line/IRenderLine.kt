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
package com.ixigua.common.danmaku.render.engine.render.layer.line

import android.graphics.Canvas
import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem

/**
 * Created by dss886 on 2019/9/22.
 */
interface IRenderLine {

    fun onLayoutChanged(width: Float, height: Float, x: Float, y: Float)

    fun addItem(playTime:Long, item: DrawItem<DanmakuData>): Boolean

    fun typesetting(playTime:Long, isPlaying: Boolean, configChanged: Boolean = false): Int

    fun getPreDrawItems(): List<DrawItem<DanmakuData>>

    fun drawBounds(canvas: Canvas)

    fun clearRender()

}