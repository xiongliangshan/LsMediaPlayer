package com.xls.player

import android.view.SurfaceView
import android.view.TextureView
import androidx.lifecycle.LifecycleOwner

abstract class CommonPlayer{

    var currentState:PlayerState = PlayerState.IDLE
    var mUrl:String? = null
    var lsConfig:LsConfig? = null
        set(value) {
            field = value
            config(value)
        }
    var callback: LsPlayerCallback? = null

    abstract fun setDataSource(uri:String)
    abstract fun setDisplay(surface:SurfaceView)
    abstract fun setDisplay(texture: TextureView)
    abstract fun prepare()
    abstract fun start()
    abstract fun pause()
    abstract fun stop()
    abstract fun seekTo(position:Long)
    abstract fun reset()
    abstract fun release()
    abstract fun setMute(isMute:Boolean)
    abstract fun isMute():Boolean

    /**
     * 设置循环播放
     */
    abstract fun setLoop(isLoop:Boolean = false)

    /**
     * 重新加载。比如网络超时时，可以重新加载。
     */
    abstract fun reload()

    abstract fun isPlaying():Boolean

    /**
     * 绑定生命周期
     */
    abstract fun bindLifecycle(owner:LifecycleOwner?)


    protected abstract fun config(config: LsConfig?)




}