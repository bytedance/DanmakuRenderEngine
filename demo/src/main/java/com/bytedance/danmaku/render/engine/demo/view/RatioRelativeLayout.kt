package com.bytedance.danmaku.render.engine.demo.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.bytedance.danmaku.render.engine.demo.R
import kotlin.math.roundToInt

class RatioRelativeLayout @JvmOverloads constructor(context: Context,
                                                    attrs: AttributeSet? = null,
                                                    defStyle: Int = 0) : RelativeLayout(context, attrs, defStyle) {

    private var mRatio = 1.0f

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RatioRelativeLayout, defStyle, 0)
        mRatio = a.getFloat(R.styleable.RatioRelativeLayout_ratioHeightByWidth, 1.0f)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec((widthSize * mRatio).roundToInt(), MeasureSpec.EXACTLY)
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
    }


}