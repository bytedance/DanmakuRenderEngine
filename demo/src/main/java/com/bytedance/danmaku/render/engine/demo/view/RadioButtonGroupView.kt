package com.bytedance.danmaku.render.engine.demo.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.bytedance.danmaku.render.engine.demo.R
import com.bytedance.danmaku.render.engine.demo.utils.dpInt
import com.bytedance.danmaku.render.engine.demo.utils.toColor

/**
 * Created by dss886 on 2021/07/02.
 */
class RadioButtonGroupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private var mCallback: ((Int, String) -> Unit)? = null
    private var mSelected: Int = -1
    private var mButtonList: MutableList<Button> = mutableListOf()

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(8.dpInt, 8.dpInt, 8.dpInt, 8.dpInt)
    }

    fun setUp(title: String, buttons: Array<String>, selected: Int = -1, callback: ((Int, String) -> Unit)?) {
        removeAllViews()
        mButtonList.clear()
        mSelected = selected
        addView(TextView(context).apply {
            text = title
            setTextColor(R.color.black.toColor)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            gravity = Gravity.CENTER
        }, LayoutParams(84.dpInt, WRAP_CONTENT).apply {
            rightMargin = 4.dpInt
        })
        buttons.forEachIndexed  { index, button ->
            addView(Button(context).apply {
                text = button
                setTypeface(null, Typeface.NORMAL)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                mButtonList.add(this)
                setOnClickListener {
                    onButtonSelected(index)
                    mCallback?.invoke(index, button)
                }
                setBackgroundResource(R.drawable.bg_button_selector)
            }, LayoutParams(0, 40.dpInt).apply {
                weight = 1f
                setMargins(4.dpInt, 4.dpInt, 4.dpInt, 4.dpInt)
            })
        }
        mCallback = callback
        onButtonSelected(selected)
    }

    private fun onButtonSelected(index: Int) {
        mSelected = index
        mButtonList.forEachIndexed { i, button ->
            button.isSelected = index == i
        }
    }

}