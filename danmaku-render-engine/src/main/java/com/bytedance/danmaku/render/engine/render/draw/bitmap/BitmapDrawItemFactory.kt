/*
 * Copyright (C) 2022 ByteDance Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bytedance.danmaku.render.engine.render.draw.bitmap

import com.bytedance.danmaku.render.engine.data.DanmakuData
import com.bytedance.danmaku.render.engine.render.draw.DrawItem
import com.bytedance.danmaku.render.engine.render.draw.IDrawItemFactory
import com.bytedance.danmaku.render.engine.utils.DRAW_TYPE_BITMAP

/**
 * Created by dss886 on 2019-05-19.
 */
class BitmapDrawItemFactory: IDrawItemFactory {

    override fun getDrawType(): Int {
        return DRAW_TYPE_BITMAP
    }

    override fun generateDrawItem(): DrawItem<out DanmakuData> {
        return BitmapDrawItem()
    }

}