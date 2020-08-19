package com.xls.player.media

import android.media.MediaPlayer
import com.xls.player.LsInfo
import com.xls.player.LsScaleMode

object MediaAdapter {

    fun transformInfo(what:Int, extra:Int):LsInfo{
        // TODO: 2020/8/17
        var code = 0
        var message = ""
        var extra = ""
        return LsInfo(code,message,extra)
    }

    fun transformScaleMode(mode: LsScaleMode): Int {
        return when (mode) {
            LsScaleMode.SCALE_ASPECT_FIT,
            LsScaleMode.SCALE_TO_FILL -> {
                MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            }
            else -> {
                MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT
            }
        }
    }
}