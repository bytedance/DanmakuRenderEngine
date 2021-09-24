package com.ixigua.common.meteor.render.layer.mask

import android.graphics.Canvas
import android.graphics.Matrix
import com.ixigua.common.meteor.control.DanmakuController
import com.ixigua.common.meteor.data.DanmakuData
import com.ixigua.common.meteor.render.IRenderLayer
import com.ixigua.common.meteor.render.cache.IDrawCachePool
import com.ixigua.common.meteor.render.draw.DrawItem
import com.ixigua.common.meteor.render.draw.mask.MaskDrawItem
import com.ixigua.common.meteor.utils.LAYER_TYPE_MASK
import com.ixigua.common.meteor.utils.LAYER_Z_INDEX_MASK

/**
 * Created by dss886 on 2021/08/20.
 */
class MaskLayer: IRenderLayer {

    private var mWidth = 0
    private var mHeight = 0
    private var mCurrentItem: DrawItem<DanmakuData>? = null
    private var mPreDrawItemList = mutableListOf<DrawItem<DanmakuData>>()
    private val mMatrix = Matrix()

    override fun init(controller: DanmakuController, cachePool: IDrawCachePool) {
        // do nothing
    }

    override fun getLayerType(): Int {
        return LAYER_TYPE_MASK
    }

    override fun getLayerZIndex(): Int {
        return LAYER_Z_INDEX_MASK
    }

    override fun onLayoutSizeChanged(width: Int, height: Int) {
        mWidth = width
        mHeight = height
        (mCurrentItem as? MaskDrawItem)?.let {
            generateDrawPath(it)
        }
    }

    override fun addItems(playTime: Long, list: List<DrawItem<DanmakuData>>) {
        if (list.isNotEmpty()) {
            mCurrentItem = list[0]
            (mCurrentItem as? MaskDrawItem)?.let {
                generateDrawPath(it)
            }
        }
    }

    override fun releaseItem(item: DrawItem<DanmakuData>) {
        // do nothing
    }

    override fun typesetting(playTime: Long, isPlaying: Boolean, configChanged: Boolean): Int {
        val data = (mCurrentItem as? MaskDrawItem)?.data
        if (data == null || data.start > playTime || data.end < playTime) {
            mCurrentItem = null
        }
        return if (mCurrentItem != null) 1 else 0
    }

    override fun drawBounds(canvas: Canvas) {
        // do nothing
    }

    override fun getPreDrawItems(): List<DrawItem<DanmakuData>> {
        mPreDrawItemList.clear()
        mCurrentItem?.let {
            mPreDrawItemList.add(it)
        }
        return mPreDrawItemList
    }

    /**
     * Scale the original svg path according to the screen size
     */
    private fun generateDrawPath(item: MaskDrawItem) {
        item.data?.let { data ->
            mMatrix.reset()
            val scaleX: Float = mWidth.toFloat() / data.pathWidth
            val scaleY: Float = mWidth.toFloat() / data.pathHeight
            if (scaleX < scaleY) {
                mMatrix.postScale(scaleX, scaleX, 0F, 0F)
            } else {
                mMatrix.postScale(scaleY, scaleY, 0F, 0F)
            }
            item.drawPath = data.path?.apply { transform(mMatrix) }
        }
    }


    override fun clear() {
        mCurrentItem = null
        mPreDrawItemList.clear()
    }

}