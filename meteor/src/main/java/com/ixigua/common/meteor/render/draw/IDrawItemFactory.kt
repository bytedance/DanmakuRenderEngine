package com.ixigua.common.meteor.render.draw

import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.data.DanmakuData

/**
 * Created by dss886 on 2019-05-19.
 * Factory for producing a specific type of DrawItem.
 * Using [DanmakuController.registerDrawItemFactory] to register your custom Factory.
 */
interface IDrawItemFactory {

    fun getDrawType(): Int

    fun generateDrawItem(): DrawItem<out DanmakuData>

}
