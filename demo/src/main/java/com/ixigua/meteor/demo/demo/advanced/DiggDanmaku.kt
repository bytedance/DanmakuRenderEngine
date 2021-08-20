package com.ixigua.meteor.demo.demo.advanced

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.ixigua.common.meteor.control.DanmakuConfig
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.draw.DrawItem
import com.ixigua.common.meteor.render.draw.bitmap.BitmapData
import com.ixigua.common.meteor.render.draw.bitmap.BitmapDrawItem
import com.ixigua.common.meteor.render.draw.text.TextData
import com.ixigua.common.meteor.render.draw.text.TextDrawItem
import com.ixigua.meteor.demo.utils.DIGG_STATE_NONE
import com.ixigua.meteor.demo.utils.DRAW_TYPE_ADVANCED
import com.ixigua.meteor.demo.utils.dp

/**
 * Created by dss886 on 2021/07/07.
 */

class DiggData: DanmakuData() {

    override var drawType: Int = DRAW_TYPE_ADVANCED

    var diggState: Int = DIGG_STATE_NONE
    var diggIcon: BitmapData? = null
    var diggText: TextData? = null

}

class DiggDrawItem : DrawItem<DiggData>() {

    override var x: Float = 0F
        set(value) {
            field = value
            updateXY()
        }
    override var y: Float = 0F
        set(value) {
            field = value
            updateXY()
        }
    override var height: Float = 16.dp

    private val mDiggIconItem: BitmapDrawItem = BitmapDrawItem()
    private val mDiggCountItem: TextDrawItem = TextDrawItem()

    private val mDiggBgPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val mDiggBgRectF = RectF()
    private var mPaddingLeft = 4.dp
    private var mPaddingRight = 8.dp
    private var mCountMarginLeft = 2.dp

    override fun getDrawType(): Int {
        return DRAW_TYPE_ADVANCED
    }

    override fun onBindData(data: DiggData) {
        data.diggIcon?.let {
            mDiggIconItem.bindData(it)
        }
        data.diggText?.let {
            it.textSize = 11.dp
            it.textStrokeWidth = 0f
            mDiggCountItem.bindData(it)
        }
    }

    override fun onMeasure(config: DanmakuConfig) {
        mDiggIconItem.measure(config)
        mDiggCountItem.measure(config)
        height = 16.dp
        width = mDiggIconItem.width + mDiggCountItem.width + mPaddingLeft + mPaddingRight
    }

    override fun onDraw(canvas: Canvas, config: DanmakuConfig) {
        if (data != null) {
            drawDiggBg(canvas)
            mDiggIconItem.draw(canvas, config)
            mDiggCountItem.draw(canvas, config)
        }
    }

    override fun recycle() {
        super.recycle()
        mDiggBgPaint.reset()
        mDiggIconItem.recycle()
        mDiggCountItem.recycle()
    }

    private fun drawDiggBg(canvas: Canvas) {
        mDiggBgRectF.set(x, y, x + width, y + height)
        mDiggBgPaint.color = Color.BLACK
        mDiggBgPaint.alpha = 66 // 26%
        canvas.drawRoundRect(mDiggBgRectF, height / 2, height / 2, mDiggBgPaint)
    }

    /**
     * Update the x/y of sub-item when the parent's x/y changed
     */
    private fun updateXY() {
        mDiggIconItem.x = x + mPaddingLeft
        mDiggIconItem.y = y + (height - mDiggIconItem.height) / 2
        mDiggCountItem.x = mDiggIconItem.x + mDiggIconItem.width + mCountMarginLeft
        mDiggCountItem.y = y + (height - mDiggCountItem.height) / 2
    }

}