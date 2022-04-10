package com.ixigua.common.danmaku.render.engine.render.draw

import com.ixigua.common.danmaku.render.engine.control.DanmakuController
import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.draw.DrawItem

/**
 * Created by dss886 on 2019-05-19.
 * Factory for producing a specific type of DrawItem.
 * Using [DanmakuController.registerDrawItemFactory] to register your custom Factory.
 */
interface IDrawItemFactory {

    fun getDrawType(): Int

    fun generateDrawItem(): DrawItem<out DanmakuData>

}
