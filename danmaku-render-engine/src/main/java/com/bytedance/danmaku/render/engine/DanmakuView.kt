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
package com.bytedance.danmaku.render.engine

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.bytedance.danmaku.render.engine.control.DanmakuController

/**
 * Created by dss886 on 2018/11/6.
 * The view danmaku will be drawn on.
 */
@Suppress("unused")
class DanmakuView @JvmOverloads constructor(context: Context,
                                            attrs: AttributeSet? = null,
                                            defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    @Suppress("MemberVisibilityCanBePrivate")
    val controller: DanmakuController = DanmakuController(this)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        controller.onLayoutSizeChanged(right - left, bottom - top)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        controller.draw(this, canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (controller.onTouchEvent(event)) {
            return true
        }
        return super.onTouchEvent(event)
    }

}
