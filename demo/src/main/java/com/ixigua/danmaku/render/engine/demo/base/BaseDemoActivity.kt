package com.ixigua.danmaku.render.engine.demo.base

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.ixigua.common.danmaku.render.engine.data.DanmakuData
import com.ixigua.common.danmaku.render.engine.render.draw.text.TextData
import com.ixigua.common.danmaku.render.engine.utils.LAYER_TYPE_SCROLL
import com.ixigua.danmaku.render.engine.demo.R
import com.ixigua.danmaku.render.engine.demo.view.CustomVideoView
import com.ixigua.danmaku.render.engine.demo.view.DanmakuRecyclerView

/**
 * Created by dss886 on 2021/07/01.
 */
abstract class BaseDemoActivity: AppCompatActivity() {

    protected lateinit var mVideoView: CustomVideoView
    protected lateinit var mDanmakuData: List<DanmakuData>

    abstract fun getLayoutId(): Int

    abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        mDanmakuData = buildDanmakuData()

        findViewById<CustomVideoView>(R.id.video_view)?.apply {
            mVideoView = this
            setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.video))
        }

        findViewById<DanmakuRecyclerView>(R.id.danmaku_recycler_view)?.apply {
            data = mDanmakuData
        }

        init()
        mVideoView.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        mVideoView.stopPlayback()
        super.onDestroy()
    }

    protected open fun buildDanmakuData(): List<DanmakuData> {
        val contents = resources.getStringArray(R.array.danmaku_text_contents)
        val list = mutableListOf<TextData>()
        for (i in 0..99) {
            list.add(TextData().apply {
                text = contents.random()
                layerType = LAYER_TYPE_SCROLL
                showAtTime = i * 600L +  (0..300L).random()
            })
        }
        return list
    }

}