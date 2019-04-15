package com.ixigua.common.meteor.render.draw

import com.ixigua.common.meteor.data.IDanmakuData

/**
 * Created by dss886 on 2019-05-19.
 */
interface IDrawItemFactory {

    fun getDrawType(): Int

    fun generateDrawItem(): IDrawItem<out IDanmakuData>

}
