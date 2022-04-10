package com.ixigua.danmaku.render.engine.demo.utils

import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.ixigua.danmaku.render.engine.demo.base.App
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dss886 on 2019-06-14.
 */

val Float.dp: Float
    get() = (App.inst().resources.displayMetrics.density * this) + 0.5f

val Float.dpInt: Int
    get() = this.dp.toInt()

val Float.px: Float
    get() = this / App.inst().resources.displayMetrics.density + 0.5f

val Int.px: Float
    get() = this.toFloat().px

val Int.dp: Float
    get() = this.toFloat().dp

val Int.dpInt: Int
    get() = this.dp.toInt()

val Float.sp: Float
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, App.inst().resources.displayMetrics)

val Float.spInt: Int
    get() = this.sp.toInt()

val Int.sp: Float
    get() = this.toFloat().sp

val Int.spInt: Int
    get() = this.toFloat().sp.toInt()

val Int.toColor: Int
    get() = ContextCompat.getColor(App.inst(), this)

fun View.setPaddingTop(padding: Int) {
    setPadding(paddingLeft, padding, paddingRight, paddingBottom)
}

fun View.visibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun Long.toDisplayDateTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.ROOT)
    return sdf.format(Date(this))
}

fun Long.toDisplayDuration(showMilliseconds: Boolean = true): String {
    var time = this

    var hours: Long = 0
    var minutes: Long = 0

    if (time >= 3600_000) {
        hours = time / 3600000
        time -= hours * 3600000
    }
    if (time >= 60_000) {
        minutes = time / 60_000
        time -= minutes * 60_000
    }

    val seconds: Long = time / 1000
    val milliSeconds: Long = time % 1000

    val hString = hours.toString().padStart(2, '0')
    val mString = minutes.toString().padStart(2, '0')
    val sString = seconds.toString().padStart(2, '0')
    val msString = milliSeconds.toString().padStart(3, '0')

    return if (showMilliseconds) {
        "$hString:$mString:$sString.$msString"
    } else {
        "$hString:$mString:$sString"
    }
}