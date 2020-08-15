package com.xls.player

import com.aliyun.player.bean.ErrorInfo

interface CommonCallback {
    /**
     * 准备成功事件
     */
    fun onPrepared()

    /**
     * 播放完成事件
     */
    fun onCompletion()

    /**
     * 出错事件
     */
    fun onError(errorInfo: ErrorInfo)

    /**
     * 缓冲开始
     */
    fun onLoadingBegin()

    /**
     * 缓冲进度
     */
    fun onLoadingProgress(percent:Int, kbps:Float)

    /**
     * 缓冲结束
     */
    fun onLoadingEnd()

    /**
     * 拖动结束
     */
    fun onSeekComplete()

    /**
     * 播放器状态改变事件
     */
    fun onStateChanged(newState:Int)
}