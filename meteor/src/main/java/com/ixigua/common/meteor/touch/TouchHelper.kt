package com.ixigua.common.meteor.touch

import android.view.MotionEvent

/**
 * Created by dss886 on 2019-05-08.
 */
class TouchHelper {

    private var mCurrentTarget: ITouchTarget? = null

    fun onTouchEvent(event: MotionEvent, delegate: ITouchDelegate): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val target = delegate.findTouchTarget(event)
                return if (target == null) false else {
                    mCurrentTarget = target
                    return target.onTouchEvent(event)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                return mCurrentTarget?.onTouchEvent(event) ?: false
            }
            MotionEvent.ACTION_UP -> {
                val handled = mCurrentTarget?.onTouchEvent(event) ?: false
                mCurrentTarget = null
                return handled
            }
        }
        return false
    }
}