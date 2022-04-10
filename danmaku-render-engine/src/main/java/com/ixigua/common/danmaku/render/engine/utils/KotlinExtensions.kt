package com.ixigua.common.danmaku.render.engine.utils

import android.os.Build
import android.view.View

/**
 * Created by dss886 on 2019/4/14.
 */

fun View.postInvalidateCompat() {
    if (Build.VERSION.SDK_INT >= 16) {
        postInvalidateOnAnimation()
    } else {
        postInvalidate()
    }
}