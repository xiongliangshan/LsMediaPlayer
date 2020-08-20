package com.xls.player

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
    fun onError(info: LsInfo)

    /**
     * 缓冲开始
     */
    fun onBufferingBegin()

    /**
     * 缓冲进度
     */
    fun onBufferingProgress(percent:Int, kbps:Float)

    /**
     * 缓冲结束
     */
    fun onBufferingEnd()

    /**
     * 拖动结束
     */
    fun onSeekComplete()

    /**
     * 播放器状态改变事件
     */
    fun onStateChanged(state: PlayerState)

    /**
     * 内部获取总时长完成，
     * 此时可以通过getDuration拿到数据了
     */
    fun onFetchDurationFinished(duration:Long)

    /**
     * 播放进度
     */
    fun onPlayProgress(percent:Int)
}