package com.ixigua.common.meteor.touch

import android.graphics.PointF
import android.graphics.RectF
import com.ixigua.common.meteor.data.DanmakuData

/**
 * Created by dss886 on 2019-05-08.
 */
interface IItemClickListener {

    fun onDanmakuClick(data: DanmakuData, itemRect: RectF, clickPoint: PointF)

}