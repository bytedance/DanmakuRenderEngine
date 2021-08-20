package com.ixigua.meteor.demo.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.draw.text.TextData
import com.ixigua.meteor.demo.R
import com.ixigua.meteor.demo.utils.toDisplayDuration
import kotlin.time.ExperimentalTime

/**
 * Created by dss886 on 2021/04/27.
 */
class DanmakuRecyclerView @JvmOverloads constructor(context: Context,
                                                    attrs: AttributeSet? = null,
                                                    defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {

    var data: List<DanmakuData>? = null
        set(value) {
            field = value
            adapter?.notifyDataSetChanged()
        }

    init {
        layoutManager = LinearLayoutManager(context)
        adapter = DanmakuAdapter()
    }

    inner class DanmakuViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    inner class DanmakuAdapter: RecyclerView.Adapter<DanmakuViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DanmakuViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_danmaku_list_item, parent, false)
            return DanmakuViewHolder(view)
        }

        @ExperimentalTime
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: DanmakuViewHolder, position: Int) {
            val danmakuData = data?.get(position) ?: return
            holder.itemView.findViewById<TextView>(R.id.time)?.let {
                it.text = danmakuData.showAtTime.toDisplayDuration()
            }
            holder.itemView.findViewById<TextView>(R.id.content)?.let {
                when (danmakuData) {
                    is TextData -> it.text = danmakuData.text
                }
            }
        }

        override fun getItemCount(): Int {
            return data?.size ?: 0
        }

    }
}