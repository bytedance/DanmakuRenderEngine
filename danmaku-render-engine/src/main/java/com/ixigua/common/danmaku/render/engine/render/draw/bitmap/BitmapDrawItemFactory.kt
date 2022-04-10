package com.ixigua.common.danmaku.render.engine.render.draw.bitmap

import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem
import com.ixigua.common.danmaku.render.engine.render.draw.IDrawItemFactory
import com.ixigua.common.danmaku.render.engine.utils.DRAW_TYPE_BITMAP

/**
 * Created by dss886 on 2019-05-19.
 */
class BitmapDrawItemFactory: IDrawItemFactory {

    override fun getDrawType(): Int {
        return DRAW_TYPE_BITMAP
    }

    override fun generateDrawItem(): DrawItem<out DanmakuData> {
        return BitmapDrawItem()
    }

}