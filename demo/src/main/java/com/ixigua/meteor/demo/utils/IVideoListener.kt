package com.ixigua.meteor.demo.utils

/**
 * Created by dss886 on 2021/07/01.
 */
interface IVideoListener {

    fun onPrepared()

    fun onVideoStart()

    fun onVideoPause()

    /**
     * @param position In milliseconds
     */
    fun onProgress(position: Long)

    fun onVideoCompletion()

    fun onVideoError()

    fun onSeekTo()

    fun onBufferStart()

    fun onBufferEnd()

    fun onRelease()

}