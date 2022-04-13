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
package com.ixigua.common.danmaku.render.engine.render.draw.text

import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import com.ixigua.common.danmaku.render.engine.control.DanmakuConfig
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem
import com.ixigua.common.danmaku.render.engine.utils.DRAW_TYPE_TEXT

/**
 * Created by dss886 on 2019/4/19.
 *
 * FontMetrics: Top - Ascent - Baseline - Descent - Bottom
 *
 * In ASCII or common Asia characters,
 * the space between Top and Ascent are usually unused,
 * which causes the text to be visually not centered.
 *
 * Turn TextData.includeFontPadding to false will cut the space between Top and Ascent.
 */
open class TextDrawItem: DrawItem<TextData>() {

    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val mUnderlinePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    override fun getDrawType(): Int {
        return DRAW_TYPE_TEXT
    }

    override fun onBindData(data: TextData) {
        mTextPaint.flags = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
        mUnderlinePaint.flags = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
    }

    override fun onMeasure(config: DanmakuConfig) {
        if (!TextUtils.isEmpty(data?.text)) {
            mTextPaint.textSize = data?.textSize ?: config.text.size
            width = mTextPaint.measureText(data?.text)
            val includeFontPadding = data?.includeFontPadding ?: config.text.includeFontPadding
            height = getFontHeight(includeFontPadding, mTextPaint)
        } else {
            width = 0F
            height = 0F
        }
    }

    override fun onDraw(canvas: Canvas, config: DanmakuConfig) {
        drawText(canvas, mTextPaint, config)
        drawUnderline(canvas, mTextPaint, mUnderlinePaint, config)
    }

    override fun recycle() {
        super.recycle()
        mTextPaint.reset()
        mUnderlinePaint.reset()
    }

    /**
     * Canvas.drawText() is positioning the text by baseline
     */
    private fun drawText(canvas: Canvas, paint: Paint, config: DanmakuConfig) {
        data?.text?.let { text ->
            // draw stroke
            (data?.textStrokeWidth ?: config.text.strokeWidth).takeIf { it > 0 }?.let { width ->
                paint.style = Paint.Style.STROKE
                paint.color = data?.textStrokeColor ?: config.text.strokeColor
                paint.typeface = data?.typeface ?: config.text.typeface
                paint.textSize = data?.textSize ?: config.text.size
                paint.strokeWidth = width
                val baseline = getBaseline(data?.includeFontPadding ?: true, y, paint)
                canvas.drawText(text, x, baseline, paint)
            }
            // draw drawText
            paint.style = Paint.Style.FILL
            paint.color = data?.textColor ?: config.text.color
            paint.typeface = data?.typeface ?: config.text.typeface
            paint.textSize = data?.textSize ?: config.text.size
            paint.strokeWidth = 0f
            val includeFontPadding = data?.includeFontPadding ?: config.text.includeFontPadding
            val baseline = getBaseline(includeFontPadding, y, paint)
            canvas.drawText(text, x, baseline, paint)
        }
    }

    private fun drawUnderline(canvas: Canvas, textPaint: Paint, underlinePaint: Paint,config: DanmakuConfig) {
        takeIf { data?.hasUnderline == true }?.let {
            val includeFontPadding = data?.includeFontPadding ?: config.text.includeFontPadding
            val underlineY = y + getFontHeight(includeFontPadding, textPaint) + config.underline.marginTop
            // draw underline stroke
            takeIf {config.underline.strokeWidth > 0 }?.let {
                underlinePaint.style = Paint.Style.STROKE
                underlinePaint.color = config.underline.strokeColor
                underlinePaint.strokeWidth = config.underline.strokeWidth
                canvas.drawRect(x, underlineY, x + width, underlineY + config.underline.width, underlinePaint)
            }
            // draw underline
            underlinePaint.style = Paint.Style.FILL
            underlinePaint.color = data?.textColor ?: config.underline.color
            underlinePaint.strokeWidth = 0f
            canvas.drawRect(x, underlineY, x + width, underlineY + config.underline.width, underlinePaint)
        }
    }

    private fun getFontHeight(includeFontPadding: Boolean, paint: Paint): Float {
        return if (includeFontPadding)
            paint.fontMetrics.bottom - paint.fontMetrics.top
        else
            paint.fontMetrics.bottom - paint.fontMetrics.ascent
    }

    private fun getBaseline(includeFontPadding: Boolean, top: Float, paint: Paint): Float {
        return if (includeFontPadding) top - paint.fontMetrics.top else top - paint.fontMetrics.ascent
    }

}