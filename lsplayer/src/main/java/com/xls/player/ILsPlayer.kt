package com.xls.player

import androidx.lifecycle.LifecycleOwner

interface ILsPlayer {

    /**
     * 设置媒体资源
     */
    fun setDataSource(uri: String)

    /**
     * 加载媒体资源，准备播放
     */
    fun prepare()

    /**
     * 加载媒体资源并开始播放
     */
    fun prepareAndStart()

    /**
     * 开始播放
     */
    fun start()

    /**
     * 暂停播放
     */
    fun pause()

    /**
     * 停止播放
     */
    fun stop()

    /**
     *快进或者快退到指定的播放位置
     */
    fun seekTo(position: Long)

    /**
     * 重置播放器
     */
    fun reset()


    /**
     * 释放播放器资源
     */
    fun release()

    /**
     * 设置静音开关
     */
    fun setMute(isMute: Boolean)

    /**
     * 是否静音
     */
    fun isMute(): Boolean

    /**
     * 设置循环播放
     */
    fun setLoop(isLoop: Boolean = false)

    /**
     * 重新加载。比如网络超时时，可以重新加载。
     */
    fun reload()

    /**
     * 是否正在播放
     */
    fun isPlaying(): Boolean

    /**
     * 绑定生命周期
     */
    fun bindLifecycle(owner: LifecycleOwner?)


    /**
     * 画面缩放模式
     */
    fun setScaleMode(mode: LsScaleMode)

    /**
     * 设置音量
     * volume：0.0~1.0
     */
    fun setVolume(volume: Float)

    /**
     * 获取媒体资源的总时长
     * 只有在prepared之后的状态才有效
     * 否则返回-1
     */
    fun getDuration():Long

    /**
     * 获取当前播放的媒体位置
     * 返回值单位ms
     */
    fun getCurrentPosition():Long

}