package com.ixigua.common.meteor.render.draw.bitmap

import com.ixigua.common.meteor.data.IDanmakuData
import com.ixigua.common.meteor.render.draw.IDrawItem
import com.ixigua.common.meteor.render.draw.IDrawItemFactory
import com.ixigua.common.meteor.utils.DRAW_TYPE_BITMAP

/**
 * Created by dss886 on 2019-05-19.
 */
class BitmapDrawItemFactory: IDrawItemFactory {

    override fun getDrawType(): Int {
        return DRAW_TYPE_BITMAP
    }

    override fun generateDrawItem(): IDrawItem<out IDanmakuData> {
        return BitmapDrawItem()
    }

}