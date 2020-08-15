package com.xls.player

import android.graphics.Bitmap
import com.aliyun.player.bean.ErrorInfo
import com.aliyun.player.bean.InfoBean
import com.aliyun.player.nativeclass.TrackInfo
import com.xls.player.CommonCallback

interface LsPlayerCallback : CommonCallback {

    /**
     * 视频分辨率变化回调
     */
    fun onVideoSizeChanged(width:Int, height:Int)

    /**
     * 首帧渲染显示事件
     */
    fun onRenderingStart()

    /**
     * 其他信息的事件，type包括了：循环播放开始，缓冲位置，当前播放位置，自动播放开始等
     */
    fun onInfo(infoBean:InfoBean?)

    /**
     * 显示字幕
     */
    fun onSubtitleShow(p0: Int,id:Long, data:String?)



    fun onSubtitleExtAdded(p0: Int, p1: String?)

    /**
     * 隐藏字幕
     */
    fun onSubtitleHide(p0: Int,id:Long)

    /**
     * 切换音视频流或者清晰度成功
     */
    fun onChangedSuccess(trackInfo: TrackInfo?)

    /**
     * 切换音视频流或者清晰度失败
     */
    fun onChangedFail(trackInfo:TrackInfo?, errorInfo: ErrorInfo?)

    /**
     * 截图事件
     */
    fun onSnapShot(bm: Bitmap?,with:Int, height:Int)
}