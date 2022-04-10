package com.ixigua.danmaku.render.engine.demo.demo.advanced

import android.graphics.Canvas
import com.ixigua.common.danmaku.render.engine.control.DanmakuConfig
import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem
import com.ixigua.common.danmaku.render.engine.render.draw.IDrawItemFactory
import com.ixigua.common.danmaku.render.engine.render.draw.text.TextData
import com.ixigua.common.danmaku.render.engine.render.draw.text.TextDrawItem
import com.ixigua.danmaku.render.engine.demo.utils.DRAW_TYPE_ADVANCED
import com.ixigua.danmaku.render.engine.demo.utils.dp

/**
 * Created by dss886 on 2021/07/07.
 */

class AdvancedDanmakuData: DanmakuData() {

    var textData: TextData? = null
    var diggData: DiggData? = null

    override var drawType: Int = DRAW_TYPE_ADVANCED
}

class AdvancedDanmakuDrawItem: DrawItem<AdvancedDanmakuData>() {

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

    private val mTextItem: TextDrawItem = TextDrawItem()
    private val mDiggItem: DiggDrawItem = DiggDrawItem()

    private val mDiggMargin: Float = 4.dp
    private var mHasDigg = false

    override fun getDrawType(): Int {
        return DRAW_TYPE_ADVANCED
    }

    override fun onBindData(data: AdvancedDanmakuData) {
        mHasDigg = data.diggData != null
        data.textData?.let {
            mTextItem.bindData(it)
        }
        data.diggData?.let {
            mDiggItem.bindData(it)
        }
    }

    override fun onMeasure(config: DanmakuConfig) {
        mTextItem.measure(config)
        mDiggItem.measure(config)
        height = mTextItem.height
        width = mTextItem.width
        mHasDigg = data?.diggData != null
        if (mHasDigg) {
            width += mDiggMargin + mDiggItem.width
        }
        updateXY()
    }

    override fun onDraw(canvas: Canvas, config: DanmakuConfig) {
        mTextItem.draw(canvas, config)
        if (mHasDigg) {
            mDiggItem.draw(canvas, config)
        }
    }

    override fun recycle() {
        super.recycle()
        mTextItem.recycle()
        mDiggItem.recycle()
    }

    private fun updateXY() {
        mTextItem.x = x
        mTextItem.y = y
        // Place diggItem to in the end of the text with margin
        mDiggItem.x = mTextItem.x + mTextItem.width + mDiggMargin
        // Place digg icon center vertically
        mDiggItem.y = y + (height - mDiggItem.height) / 2
    }

}

class AdvancedDanmakuFactory: IDrawItemFactory {

    override fun getDrawType(): Int {
        return DRAW_TYPE_ADVANCED
    }

    override fun generateDrawItem(): DrawItem<out DanmakuData> {
        return AdvancedDanmakuDrawItem()
    }

}