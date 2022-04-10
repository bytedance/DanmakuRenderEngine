package com.ixigua.common.danmaku.render.engine.touch

import android.graphics.PointF
import android.graphics.RectF
import com.ixigua.common.danmaku.render.engine.data.DanmakuData

/**
 * Created by dss886 on 2019-05-08.
 */
interface IItemClickListener {

    fun onDanmakuClick(data: DanmakuData, itemRect: RectF, clickPoint: PointF)

}