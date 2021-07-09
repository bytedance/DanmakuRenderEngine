package com.ixigua.common.meteor.render.draw.bitmap

import android.graphics.*
import com.ixigua.common.meteor.control.DanmakuConfig
import com.ixigua.common.meteor.render.draw.DrawItem
import com.ixigua.common.meteor.utils.DRAW_TYPE_BITMAP

/**
 * Created by dss886 on 2019-05-20.
 */
open class BitmapDrawItem: DrawItem<BitmapData>() {

    private val mBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val mRectF = RectF()

    override fun getDrawType(): Int {
        return DRAW_TYPE_BITMAP
    }

    override fun bindData(data: BitmapData) {
        this.data = data
        mBitmapPaint.flags = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
    }

    override fun measure(config: DanmakuConfig) {
        val width = data?.width ?: 0F
        val height = data?.height ?: 0F
        if (width > 0 && height > 0) {
            this.width = width
            this.height = height
        } else {
            this.width = data?.bitmap?.width?.toFloat() ?: 0F
            this.height = data?.bitmap?.height?.toFloat() ?: 0F
        }
    }

    override fun draw(canvas: Canvas, config: DanmakuConfig) {
        data?.bitmap?.let { bitmap ->
            mRectF.set(x, y, x + width, y + height)
            val tint = data?.tintColor
            mBitmapPaint.colorFilter = if (tint == null) null else PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, null, mRectF, mBitmapPaint)
        }
    }

    override fun recycle() {
        super.recycle()
        mBitmapPaint.reset()
        mRectF.set(0F, 0F, 0F, 0F)
    }
}