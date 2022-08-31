package com.bytedance.danmaku.render.engine.demo.view

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.MEDIA_INFO_BUFFERING_END
import android.media.MediaPlayer.MEDIA_INFO_BUFFERING_START
import android.os.Build
import android.util.AttributeSet
import android.widget.VideoView
import com.bytedance.danmaku.render.engine.demo.base.App
import com.bytedance.danmaku.render.engine.demo.utils.IVideoListener
import java.util.*

/**
 * Created by dss886 on 2021/07/01.
 * Add some listener support to the VideoView
 */
class CustomVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : VideoView(context, attrs, defStyle) {

    var videoListener: IVideoListener? = null
    private var mTimer: Timer? = null
    private var mMediaPlayer: MediaPlayer? = null

    init {
        setOnPreparedListener {
            mMediaPlayer = it
            videoListener?.onPrepared()
            mTimer = Timer()
            mTimer?.schedule(object: TimerTask() {
                override fun run() {
                    App.inst().mainHandler.post {
                        if (isPlaying) {
                            videoListener?.onProgress(currentPosition.toLong())
                        }
                    }
                }
            }, 0L, 500L)
        }
        setOnErrorListener { _, _, _ ->
            videoListener?.onVideoError()
            true
        }
        setOnCompletionListener {
            videoListener?.onProgress(duration.toLong())
            videoListener?.onVideoCompletion()
        }
        setOnInfoListener { _, what, _ ->
            when (what) {
                MEDIA_INFO_BUFFERING_START -> videoListener?.onBufferStart()
                MEDIA_INFO_BUFFERING_END -> videoListener?.onBufferEnd()
            }
            true
        }
    }

    fun setPlaySpeed(speed: Float) {
        mMediaPlayer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.playbackParams = it.playbackParams.setSpeed(speed)
            }
        }
    }

    override fun pause() {
        super.pause()
        videoListener?.onVideoPause()
    }

    override fun start() {
        super.start()
        videoListener?.onVideoStart()
    }

    override fun seekTo(msec: Int) {
        super.seekTo(msec)
        videoListener?.onSeekTo()
    }

    override fun stopPlayback() {
        super.stopPlayback()
        mMediaPlayer = null
        videoListener?.onRelease()
        mTimer?.cancel()
    }

}