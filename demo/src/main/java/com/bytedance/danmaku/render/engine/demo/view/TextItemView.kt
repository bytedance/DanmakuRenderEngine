package com.bytedance.danmaku.render.engine.demo.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bytedance.danmaku.render.engine.demo.R
import com.bytedance.danmaku.render.engine.demo.utils.visibleOrGone

/**
 * Created by dss886 on 2021/02/11.
 */
class TextItemView @JvmOverloads constructor(context: Context,
                                             attrs: AttributeSet? = null,
                                             defStyle: Int = 0) : LinearLayout(context, attrs, defStyle) {

    init {
        inflate(getContext(), R.layout.view_text_item, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextItemView)
        val title = typedArray.getString(R.styleable.TextItemView_title)
        val content = typedArray.getString(R.styleable.TextItemView_content)
        val hideDivider = typedArray.getBoolean(R.styleable.TextItemView_hideDivider, false)
        val hideRightArrow = typedArray.getBoolean(R.styleable.TextItemView_hideRightArrow, false)
        typedArray.recycle()

        val titleView: TextView = findViewById(R.id.title)
        val contentView: TextView = findViewById(R.id.content)
        val dividerView: View = findViewById(R.id.divider)
        val rightArrowView: View = findViewById(R.id.right_arrow)

        titleView.text = title
        contentView.text = content
        dividerView.visibleOrGone(!hideDivider)
        rightArrowView.visibleOrGone(!hideRightArrow)
    }

}