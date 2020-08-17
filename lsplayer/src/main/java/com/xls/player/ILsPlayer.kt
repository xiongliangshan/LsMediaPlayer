package com.xls.player

import androidx.lifecycle.LifecycleOwner

interface ILsPlayer {

     fun setDataSource(uri:String)
     fun prepare()
     fun start()
     fun pause()
     fun stop()
     fun seekTo(position:Long)
     fun reset()
     fun release()
     fun setMute(isMute:Boolean)
     fun isMute():Boolean

    /**
     * 设置循环播放
     */
     fun setLoop(isLoop:Boolean = false)

    /**
     * 重新加载。比如网络超时时，可以重新加载。
     */
     fun reload()

     fun isPlaying():Boolean

    /**
     * 绑定生命周期
     */
     fun bindLifecycle(owner: LifecycleOwner?)


     fun config(config: LsConfig?)

    /**
     * 画面缩放模式
     */
     fun setScaleMode(mode:LsScaleMode)

}