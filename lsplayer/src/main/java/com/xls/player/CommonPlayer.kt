package com.xls.player

import android.view.SurfaceView
import android.view.TextureView

abstract class CommonPlayer:ILsPlayer{

    var currentState:PlayerState = PlayerState.IDLE
    var mUrl:String? = null
    var lsConfig:LsConfig? = null
        set(value) {
            field = value
            config(value)
        }
    var callback: LsPlayerCallback? = null


    abstract fun setDisplay(surface: SurfaceView)
    abstract fun setDisplay(texture: TextureView)
}