package com.ixigua.common.meteor.render.draw

import android.graphics.Canvas
import com.ixigua.common.meteor.control.DanmakuConfig
import com.ixigua.common.meteor.data.IDanmakuData

/**
 * Created by dss886 on 2019-05-13.
 */
interface IDrawItem<T: IDanmakuData> {

    var data: T?
    var x: Float
    var y: Float
    var width: Float
    var height: Float
    var isPaused: Boolean

    fun getDrawType(): Int

    fun bindData(data: T)

    fun measure(config: DanmakuConfig)

    fun draw(canvas: Canvas, config: DanmakuConfig)

    fun recycle() {
        data = null
        x = 0F
        y = 0F
        width = 0F
        height = 0F
        isPaused = false
    }

}