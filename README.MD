# Meteor

[简体中文版说明 >>>](/README_cn.md)

[![GitHub license](https://img.shields.io/github/license/bytedance/scene)](/LICENSE)
[![API](https://img.shields.io/badge/api-14%2B-green)](https://developer.android.com/about/dashboards)

Meteor is a lightweight and scalable danmaku library.

1. High-performance rendering and low memory usage
2. Easy-to-use interface with high scalability
3. Support complex custom danmaku styles 
4. Support custom danmaku motion modes

[Download the Demo >>>]()

## Apps Using Meteor

| <img src="misc/xigua.png" alt="xigua" width="100"/> | <img src="misc/toutiao.png" alt="toutiao" width="100"/> | <img src="misc/dongchedi.png" alt="dongchedi" width="100"/> |
|:-----------:|:-------:|:-----:|
| Xigua Video | Toutiao | DongCheDi |

## Quick Startup

Add dependency to your build.gradle:

```gradle
implementation 'com.ixigua.common:meteor:$latest_version'
```

In the simplest case, it only takes four steps to start playing danmakus:

1. Place `DanmakuView` to the same size as the video view in the XML layout
2. Get the `DanmakuController` through the `danmakuView.controller` method
3. Construct a `TextData` for each danmaku. Only the fields of `text`, `LayerType` and `showAtTime` need to be filled in by default
4. Call `controller.setData()` to set the data, and then use `controller.start()` to start playing

## Timeline Synchronization

### Control the Danmaku

`DanmakuController` provides four methods related to the timeline control:

```
fun start(playTime: Long)  // Play or resume from a paused state.
fun pause()                // Pause and the danmakus on the screen will stop in place.
fun stop()                 // Stop playing which will clear the danmakus on the screen and reset the timeline.
fun clear()                // Clear the danmakus that has been displayed on the screen alone.
```

### Play Synchronization

According to the industry's common implementation logic, the callback of the player and methods of `DanmakuController` are corresponding as following which can be customized to meet specific needs.

| Callback of the Player | DanmakuController | Comment |
|:--------:|:--------------------:|:----:|
|RenderStart|start||
|VideoPause|pause||
|VideoResume|start||
|VideoStop|pause|Let the rest danmakus stay in place after the video is played over|
|VideoReplay|stop||
|VideoError|pause||
|onSeekStart|pause||
|onSeekComplete|clear; if (isPlaying) {start}|The screen should be cleared after seek completed|
|onBufferStart|pause||
|onBufferEnd|start||

### Other Forms of Business

Meteor library is not coupled with the video. If you want to achieve a picture danmaku similar to Qzone, the approach is similar.

## Custom Motion Modes

The rendering structure of Meteor is divided into different layers by motion modes. Each rendering layer manages the layout and rendering by itself, and they are controlled by the `z-index` attribute.

Meteor supports three motion modes by default:

1. ScrollLayer: scroll from the right to the left
2. TopCenterLayer: display in the top middle of the screen
3. BottomCenterLayer: display in the bottom middle of the screen

Implement `IRenderLayer` interface to develop your custom layer and then call `DanmakuController.addRenderLayer()` to register it. After that, Meteor will automatically delivery every `DanmakuData` with the specify `LayerType` to the corresponding `Layer` for typesetting and drawing.

## Custom Danmaku Style

### Definition

Meteor library divides a danmaku into two part during design: a data entity (DanmakuData) and a drawing entity (DrawItem):

1. DanmakuData only contains the data attribute of this danmaku, which will not change unless modified manually
2. DrawItem defines how this danmaku will be drawn, and also contains the attributes will be using by drawing (x/y/width/height, etc.)
3. DrawItem properties will be continuously modified by RenderLayer logic during the rendering process, and will be recycled and reused after it been moved out of the screen

To make a custom danmaku you need to inherit the three classes below:

1. CustomDanmakuData
2. CustomDrawItem
3. CustomDrawItemFactory

Then call `DanmakuController.registerDrawItemFactory()` to register your custom factory, and Meteor will use the registered factory to generate a `CustomDrawItem` automatically when there is a custom `CustomDanmakuData`.

### Implementation

Based on the idea that combination is better than inheritance, Meteor only provides two types of danmaku by default: text (TextData) and picture (BitmapData).
Most advanced types of danmaku can be implemented by combining the two basic ones.

The custom DanmakuData and DrawItem need to be defined by the same structure. When the x, y and other attributes is changing, the new value will be distributed downward.

Take the danmaku style of the Xigua Video as an example, it has three parts: text, like icon and the number of likes:

![Custom Danmaku](misc/danmaku1.png)

The structure combination of DanmakuData is as follows:

![Danmaku Data](misc/danmaku2.png)

Note that the canvas is accessible in the DrawItem which means you can draw directly with it. The background of the above-mentioned like icon is drawn by the canvas.

## Danmaku Configuration

### Requirements

Under typical circumstances, we have to modify the properties of danmaku sometimes:

1. The font size and line height of a danmaku should be larger after entering the full screen, and smaller after exiting.
2. The font color of a danmaku should be changed after being give a like.
3. Changing the font size, transparency, etc. of all danmaku from the setting panel.

Similar requirements can mainly achieved through the `DanmakuController.config` in Meteor.

### Cascade and Priority

Meteor is designed to use the concept of cascade and priority in configuration similar to CSS according to actual needs.

`Config` is the global configuration with the lowest priority, which is divided into:

```kotlin
val debug = DebugConfig(this)
val common = CommonConfig(this)
val text = TextConfig(this)
val underline = UnderlineConfig(this)
val scroll = ScrollLayerConfig(this)
val top = TopCenterLayerConfig(this)
val bottom = BottomCenterLayerConfig(this)
```

For example, `TextConfig` in `Config` control the global `size` and `color` of all text. Meanwhile the `TextData` of each danmaku can also control the `TextSize` and `TextColor` of itself which has higher priority.

If a property is defined in `TextData`, then this one takes effect.\
If it is not defined in `TextData`, then the one defined in `Config` takes effect.

## Commands and Events

For a danmaku library, there are usually two requirements:

1. Let the library to do some things, such as:
    1. pause a danmaku.
    2. change the color of a danmaku.
2. The library informs the outside that something has happened, such as:
    1. a danmaku has been displayed.
    2. a danmaku has been removed from the screen.
    3. the size of a danmaku changed.

In response to these requirements, Meteor has designed two sets of mechanisms: **command** and **event**

### Command

`DanmakuCommand` is used to let Meteor to do something which main entrance is`DanmakuController.executeCommand()`, such as:

```kotlin
mController?.executeCommand(CMD_PAUSE_ITEM, data)           // pause a danmaku
mController?.executeCommand(CMD_RESUME_ITEM, data)          // resume a danmaku
mController?.executeCommand(CMD_SET_TOUCHABLE, null, false) // make danmaku view unclickable
```

### Event

`DanmakuEvent` is used to notify something has happend, you can use the method of `DanmakuController.addEventListener()` to register listeners. And use the `DanmakuEvent.what` to determine the type of `Event`, for example:

```kotlin
override fun onEvent(event: DanmakuEvent) {
    when (event.what) {
        EVENT_DANMAKU_SHOW -> {}     // event.data shows
        EVENT_DANMAKU_DISMISS -> {}  // event.data dismisses
        EVENT_DANMAKU_REMEASURE -> {
            (event.param as? RectF)?.let { rectF ->
                // event.data has been remeasured and the new size is rectF
            }
        }
    }
}
```

## Masked Danmaku

"Masked Danmaku" is an advanced feature that can achieve the effect of "not blocking the portrait". After turning on this function, the danmakus will no longer cover the portrait, but will pass through behind the human body.

Meteor has built-in the realization of "Masked Danmaku", and it only takes a few simple steps to turn it on.

1. Analyze the content of the video content in advance, and calculate the closed SVG path that needs to be cut out for each frame.
2. Assemble each mask `Path` data into a `MaskData` structure.
3. Set these `MaskData` to `DanmakuController` like other danmaku data
4. Turn on the switch: `DanmakuController.config.mask.enable = true`

（The above code can refer to [SmartMaskActivity.kt](/demo/src/main/java/com/ixigua/meteor/demo/demo/SmartMaskActivity.kt)）

Requires attention:

1. Meteor only provides the implementation of "Masked Danmaku" on Android, and does not provide the function of content analyzing or mask path generating. You need to analyzing the screen content on server side by using AI tools in advance and deliver the data to client.
2. Meteor has no restrictions on the frame rate of the `MaskData`. But it is recommended not to be higher than 30Hz for performance and data transmission considerations (the effect is already good enough when greater than 10Hz). At the same time, Meteor also supports dynamic frame rate masks by specifying the start and end time of each `MaskData`.

## Q&A

#### Q1：Can I dynamically insert the `DanmakuView`?

A：Yes. You can create a `DanmakuView` and overlay it on the top of the video screen whenever you want, as long as the size of `DanmakuView` and the video screen are always the same (for example, when switching full screen state).

#### Q2：Can I set or add danmaku data after play it?

A：Yes. But the danmakus which `showAtTime` is before the set time will not be displayed this time (if you seek forward, they can still be displayed).
        
### License

    Copyright (C) 2021 Bytedance Inc

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/