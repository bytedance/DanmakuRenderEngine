package com.ixigua.common.danmaku.render.engine.render.draw.text

import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem
import com.ixigua.common.danmaku.render.engine.render.draw.IDrawItemFactory
import com.ixigua.common.danmaku.render.engine.utils.DRAW_TYPE_TEXT

/**
 * Created by dss886 on 2019-05-19.
 */
class TextDrawItemFactory: IDrawItemFactory {

    override fun getDrawType(): Int {
        return DRAW_TYPE_TEXT
    }

    override fun generateDrawItem(): DrawItem<out DanmakuData> {
        return TextDrawItem()
    }

}