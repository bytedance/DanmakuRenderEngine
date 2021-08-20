package com.ixigua.meteor.demo.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.ixigua.meteor.demo.R
import com.ixigua.meteor.demo.utils.setPaddingTop
import com.ixigua.meteor.demo.utils.visibleOrGone

/**
 * Created by dss886 on 2021/02/11.
 */
class SectionItemView @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyle: Int = 0) : LinearLayout(context, attrs, defStyle) {

    init {
        inflate(getContext(), R.layout.view_section_item, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SectionItemView)
        val title = typedArray.getString(R.styleable.SectionItemView_title)
        val hideDivider = typedArray.getBoolean(R.styleable.SectionItemView_hideDivider, false)
        val hideTopPadding = typedArray.getBoolean(R.styleable.SectionItemView_hideTopPadding, false)
        typedArray.recycle()

        val rootView: View = findViewById(R.id.root)
        val titleView: TextView = findViewById(R.id.title)
        val dividerView: View = findViewById(R.id.divider)

        titleView.text = title
        rootView.setPaddingTop(if (hideTopPadding) 0 else titleView.paddingTop)
        dividerView.visibleOrGone(!hideDivider)
    }

}