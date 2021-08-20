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
import com.ixigua.meteor.demo.R
import com.ixigua.meteor.demo.utils.toDisplayDateTime
import kotlin.time.ExperimentalTime

/**
 * Created by dss886 on 2021/04/27.
 */
class ConsoleRecyclerView @JvmOverloads constructor(context: Context,
                                                    attrs: AttributeSet? = null,
                                                    defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {

    private var mData: MutableList<Info> = mutableListOf()

    init {
        layoutManager = LinearLayoutManager(context)
        adapter = ConsoleAdapter()
    }

    fun log(tag: String, content: String) {
        mData.add(Info(System.currentTimeMillis(), "[${tag}]", content))
        adapter?.notifyDataSetChanged()
    }

    data class Info(val timestamp: Long, val tag: String, val content: String)

    inner class ConsoleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    inner class ConsoleAdapter: RecyclerView.Adapter<ConsoleViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsoleViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_console_list_item, parent, false)
            return ConsoleViewHolder(view)
        }

        @ExperimentalTime
        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ConsoleViewHolder, position: Int) {
            val data = mData.asReversed()[position]
            holder.itemView.findViewById<TextView>(R.id.time)?.let {
                it.text = data.timestamp.toDisplayDateTime()
            }
            holder.itemView.findViewById<TextView>(R.id.tag)?.let {
                it.text = data.tag
            }
            holder.itemView.findViewById<TextView>(R.id.content)?.let {
                it.text = data.content
            }
        }

        override fun getItemCount(): Int {
            return mData.size
        }

    }
}