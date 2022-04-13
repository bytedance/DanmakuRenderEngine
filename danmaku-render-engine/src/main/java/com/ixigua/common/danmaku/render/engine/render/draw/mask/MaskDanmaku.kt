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
package com.ixigua.common.danmaku.render.engine.render.draw.mask

import android.graphics.*
import com.ixigua.common.danmaku.render.engine.control.DanmakuConfig
import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem
import com.ixigua.common.danmaku.render.engine.render.draw.IDrawItemFactory
import com.ixigua.common.danmaku.render.engine.utils.DRAW_TYPE_MASK
import com.ixigua.common.danmaku.render.engine.utils.LAYER_TYPE_MASK

/**
 * Created by dss886 on 2021/08/20.
 */
class MaskData: DanmakuData() {
    override var drawType: Int = DRAW_TYPE_MASK
    override var layerType: Int = LAYER_TYPE_MASK

    var path: Path? = null
    var pathHeight: Int = 0
    var pathWidth: Int = 0
    var start: Long = 0L
    var end: Long = 0L
}

class MaskDrawItem: DrawItem<MaskData>() {
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val mXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

    var drawPath: Path? = null

    override fun getDrawType(): Int {
        return DRAW_TYPE_MASK
    }

    override fun onBindData(data: MaskData) {
        // do nothing
    }

    override fun onMeasure(config: DanmakuConfig) {
        // do nothing
    }

    override fun onDraw(canvas: Canvas, config: DanmakuConfig) {
        mPaint.xfermode = mXfermode
        data?.let { mask ->
            mask.path?.let { path ->
                canvas.drawPath(path, mPaint)
            }
        }
        mPaint.xfermode = null
    }
}

class MaskDanmakuFactory: IDrawItemFactory {
    override fun getDrawType(): Int {
        return DRAW_TYPE_MASK
    }

    override fun generateDrawItem(): DrawItem<out DanmakuData> {
        return MaskDrawItem()
    }
}