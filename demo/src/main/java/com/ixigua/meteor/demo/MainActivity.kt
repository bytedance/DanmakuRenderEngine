package com.ixigua.meteor.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ixigua.meteor.demo.demo.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.simplest_demo).setOnClickListener(this)
        findViewById<View>(R.id.timeline_sync).setOnClickListener(this)
        findViewById<View>(R.id.build_in_layers).setOnClickListener(this)
        findViewById<View>(R.id.style_config).setOnClickListener(this)
        findViewById<View>(R.id.custom_advanced_danmaku).setOnClickListener(this)
        findViewById<View>(R.id.custom_layer).setOnClickListener(this)
        findViewById<View>(R.id.command_and_event).setOnClickListener(this)
        findViewById<View>(R.id.smart_mask).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.simplest_demo -> startActivity(Intent(this, SimplestDemoActivity::class.java))
            R.id.timeline_sync -> startActivity(Intent(this, TimelineSyncActivity::class.java))
            R.id.build_in_layers -> startActivity(Intent(this, BuildInLayersActivity::class.java))
            R.id.style_config -> startActivity(Intent(this, StyleConfigActivity::class.java))
            R.id.custom_advanced_danmaku -> startActivity(Intent(this, AdvancedDanmakuActivity::class.java))
            R.id.custom_layer -> startActivity(Intent(this, CustomLayerActivity::class.java))
            R.id.command_and_event -> startActivity(Intent(this, CmdEventActivity::class.java))
            R.id.smart_mask -> startActivity(Intent(this, SmartMaskActivity::class.java))
        }
    }

}