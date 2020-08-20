package com.xls.player

import android.graphics.Bitmap
import com.aliyun.player.bean.InfoBean

open class SimpleLsPlayerCallback :LsPlayerCallback{
    override fun onVideoSizeChanged(width: Int, height: Int) {

    }

    override fun onRenderingStart() {

    }

    override fun onInfo(info: LsInfo?) {

    }

    override fun onSnapShot(bm: Bitmap?, with: Int, height: Int) {

    }

    override fun onPrepared() {

    }

    override fun onCompletion() {

    }

    override fun onError(info: LsInfo) {

    }

    override fun onBufferingBegin() {

    }

    override fun onBufferingProgress(percent: Int, kbps: Float) {

    }

    override fun onBufferingEnd() {

    }

    override fun onSeekComplete() {

    }

    override fun onStateChanged(state: PlayerState) {

    }

    override fun onFetchDurationFinished(duration: Long) {

    }

    override fun onPlayProgress(percent: Int) {

    }

}