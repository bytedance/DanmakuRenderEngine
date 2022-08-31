package com.bytedance.danmaku.render.engine.demo.base

import android.app.Application
import android.os.Handler
import android.os.Looper

/**
 * Created by dss886 on 2017/6/29.
 */
class App : Application() {

    companion object {
        private lateinit var inst: App

        @JvmStatic
        fun inst(): App {
            return inst
        }
    }

    lateinit var mainHandler : Handler

    override fun onCreate() {
        super.onCreate()
        inst = this
        mainHandler = Handler(Looper.getMainLooper())
    }

}