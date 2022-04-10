package com.ixigua.danmaku.render.engine.demo.demo

import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import com.ixigua.common.danmaku.render.engine.DanmakuView
import com.ixigua.common.danmaku.render.engine.control.DanmakuController
import com.ixigua.danmaku.render.engine.demo.R
import com.ixigua.danmaku.render.engine.demo.base.BaseDemoActivity
import com.ixigua.danmaku.render.engine.demo.utils.IVideoListener
import com.ixigua.danmaku.render.engine.demo.utils.toDisplayDuration

/**
 * Created by dss886 on 2021/04/20.
 */
class TimelineSyncActivity : BaseDemoActivity(), IVideoListener {

    private lateinit var mController: DanmakuController
    private lateinit var mPlayIcon: ImageView
    private lateinit var mSeekBar: AppCompatSeekBar
    private lateinit var mTimeStart: TextView
    private lateinit var mTimeEnd: TextView

    private var mIsSeeking: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.activity_timeline_sync
    }

    override fun init() {
        mPlayIcon = findViewById(R.id.play_icon)
        mTimeStart = findViewById(R.id.time_start)
        mTimeEnd = findViewById(R.id.time_end)
        mSeekBar = findViewById(R.id.seek_bar)
        mController = findViewById<DanmakuView>(R.id.danmaku_view).controller
        mController.setData(mDanmakuData)
        mVideoView.videoListener = this

        mPlayIcon.setOnClickListener {
            if (mVideoView.isPlaying) {
                mVideoView.pause()
            } else {
                mVideoView.start()
            }
        }
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mTimeStart.text = progress.toLong().toDisplayDuration(false)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mIsSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar ?: return
                mIsSeeking = false
                mVideoView.seekTo(seekBar.progress)
            }
        })
    }

    override fun onPrepared() {
        mTimeEnd.text = mVideoView.duration.toLong().toDisplayDuration(false)
        mSeekBar.max = mVideoView.duration
    }

    override fun onVideoStart() {
        mPlayIcon.setImageResource(R.drawable.ic_pause)
        mController.start()
    }

    override fun onVideoPause() {
        mPlayIcon.setImageResource(R.drawable.ic_play)
        mController.pause()
    }

    override fun onProgress(position: Long) {
        if (!mIsSeeking) {
            mSeekBar.progress = position.toInt()
            mTimeStart.text = position.toDisplayDuration(false)
        }
    }

    override fun onVideoCompletion() {
        mPlayIcon.setImageResource(R.drawable.ic_replay)
        mController.pause()
    }

    override fun onVideoError() {
        mPlayIcon.setImageResource(R.drawable.ic_play)
        mController.pause()
    }

    override fun onSeekTo() {
        mController.clear()
        if (mVideoView.isPlaying) {
            mController.start(mVideoView.currentPosition.toLong())
        }
    }

    override fun onBufferStart() {
        mController.pause()
    }

    override fun onBufferEnd() {
        mController.start(mVideoView.currentPosition.toLong())
    }

    override fun onRelease() {
        mController.stop()
    }

}