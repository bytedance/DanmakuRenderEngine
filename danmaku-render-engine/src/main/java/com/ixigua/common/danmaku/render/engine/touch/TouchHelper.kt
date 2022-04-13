/*
 * Copyright (C) 2022 ByteDance Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ixigua.common.danmaku.render.engine.touch

import android.view.MotionEvent
import com.ixigua.common.danmaku.render.engine.touch.ITouchDelegate
import com.ixigua.common.danmaku.render.engine.touch.ITouchTarget

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
                    return true
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