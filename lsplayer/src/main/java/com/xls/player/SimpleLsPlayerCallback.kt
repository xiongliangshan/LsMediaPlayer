package com.xls.player

import android.graphics.Bitmap
import com.aliyun.player.bean.ErrorInfo
import com.aliyun.player.bean.InfoBean
import com.aliyun.player.nativeclass.TrackInfo

open class SimpleLsPlayerCallback :LsPlayerCallback{
    override fun onVideoSizeChanged(width: Int, height: Int) {

    }

    override fun onRenderingStart() {

    }

    override fun onInfo(infoBean: InfoBean?) {
    }

    override fun onSubtitleShow(p0: Int, id: Long, data: String?) {

    }

    override fun onSubtitleExtAdded(p0: Int, p1: String?) {

    }

    override fun onSubtitleHide(p0: Int, id: Long) {

    }

    override fun onChangedSuccess(trackInfo: TrackInfo?) {

    }

    override fun onChangedFail(trackInfo: TrackInfo?, errorInfo: ErrorInfo?) {

    }

    override fun onSnapShot(bm: Bitmap?, with: Int, height: Int) {

    }

    override fun onPrepared() {

    }

    override fun onCompletion() {

    }

    override fun onError(errorInfo: LsErrorInfo) {

    }

    override fun onLoadingBegin() {

    }

    override fun onLoadingProgress(percent: Int, kbps: Float) {

    }

    override fun onLoadingEnd() {

    }

    override fun onSeekComplete() {

    }

    override fun onStateChanged(newState: Int) {

    }
}