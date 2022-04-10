package com.ixigua.common.danmaku.render.engine.touch

import android.view.MotionEvent

/**
 * Created by dss886 on 2019-05-08.
 */
interface ITouchDelegate {

    fun findTouchTarget(event: MotionEvent): ITouchTarget?

}