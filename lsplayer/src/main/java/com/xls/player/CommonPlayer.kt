package com.xls.player

import android.view.SurfaceView
import android.view.TextureView

abstract class CommonPlayer:ILsPlayer{

    var currentState:PlayerState = PlayerState.IDLE
    var mUrl:String? = null
    var lsConfig:LsConfig? = null
    var callback: LsPlayerCallback? = null
    var isLsAutoPlay  = true
    var lsDuration = 0L

    override fun prepare() {
        config(lsConfig)
    }


    abstract fun setDisplay(surface: SurfaceView)
    abstract fun setDisplay(texture: TextureView)
    abstract fun config(config: LsConfig?)
}