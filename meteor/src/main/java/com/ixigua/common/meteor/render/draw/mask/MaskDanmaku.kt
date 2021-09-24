package com.ixigua.common.meteor.render.draw.mask

import android.graphics.*
import com.ixigua.common.meteor.control.DanmakuConfig
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.draw.DrawItem
import com.ixigua.common.meteor.render.draw.IDrawItemFactory
import com.ixigua.common.meteor.utils.DRAW_TYPE_MASK
import com.ixigua.common.meteor.utils.LAYER_TYPE_MASK

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