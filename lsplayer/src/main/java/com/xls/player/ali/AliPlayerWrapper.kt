package com.xls.player.ali

import android.content.Context
import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.aliyun.player.AliPlayer
import com.aliyun.player.AliPlayerFactory
import com.aliyun.player.IPlayer
import com.aliyun.player.bean.InfoCode
import com.aliyun.player.source.UrlSource
import com.xls.player.*
import com.xls.player.log.SLog

class AliPlayerWrapper(context:Context): CommonPlayer() {

    private var aliPlayer:AliPlayer = AliPlayerFactory.createAliPlayer(context.applicationContext)
    private var mCurrentPosition:Long = 0L
    init {
        setListeners()
        SLog.i(PlayerEngine.TAG,"create player instance : AliPlayer")
    }

    override fun config(config: LsConfig?) {
        SLog.i(PlayerEngine.TAG,"config $config")
        aliPlayer.isAutoPlay = false
        aliPlayer.isLoop = false
        config?.let {
            aliPlayer.isLoop = it.isLoop
            it.cacheConfig?.run {
                aliPlayer.setCacheConfig(AliAdapter.transformCacheConfig(this))
            }
            it.netConfig?.run {
                val aliConfig = aliPlayer.config
                aliConfig.mNetworkTimeout = this.networkTimeout
                aliConfig.mNetworkRetryCount = this.networkRetryCount
                aliPlayer.config = aliConfig
            }
        }
    }

    private fun setListeners(){
        aliPlayer.setOnPreparedListener {
            callback?.onPrepared()
            lsDuration = getDuration()
            callback?.onFetchDurationFinished(lsDuration)
            if(isLsAutoPlay){
                aliPlayer.start()
            }
            SLog.i(PlayerEngine.TAG,"onPrepared, url = $mUrl duration = $lsDuration ms")
        }
        aliPlayer.setOnCompletionListener{
            SLog.i(PlayerEngine.TAG,"onCompletion")
            callback?.onCompletion()
        }
        aliPlayer.setOnErrorListener{
            SLog.e(PlayerEngine.TAG,"onError ${it.code} | ${it.msg} | ${it.extra}")
            callback?.onError(LsInfo(it.code.value,it.msg,it.extra))
        }
        aliPlayer.setOnVideoSizeChangedListener { p0, p1 ->
            SLog.i(PlayerEngine.TAG, "onVideoSizeChanged,width = $p0 height = $p1")
            callback?.onVideoSizeChanged(p0, p1)
        }
        aliPlayer.setOnRenderingStartListener {
            SLog.i(PlayerEngine.TAG,"onRenderingStart")
            callback?.onRenderingStart()
        }
        aliPlayer.setOnInfoListener { infoBean ->
            callback?.onInfo(AliAdapter.transformInfo(infoBean))
            when(infoBean.code){
                InfoCode.CacheSuccess ->{
                    //缓存成功
                    SLog.i(PlayerEngine.TAG,"onInfo,cache success")
                }
                InfoCode.CacheError ->{
                    //缓存失败
                    SLog.w(PlayerEngine.TAG,"onInfo,cache failed:${infoBean?.extraMsg}")
                }
                InfoCode.SwitchToSoftwareVideoDecoder ->{
                    //切换到软解
                    SLog.i(PlayerEngine.TAG,"onInfo,switch to software video decoder")
                }
                InfoCode.AudioCodecNotSupport ->{
                    //音频解码格式不支持
                    SLog.e(PlayerEngine.TAG,"onInfo,audio codec not support:${infoBean?.extraMsg}")
                }
                InfoCode.AudioDecoderDeviceError ->{
                    //音频解码失败
                    SLog.e(PlayerEngine.TAG,"onInfo,audio decoder device error:${infoBean?.extraMsg}")
                }
                InfoCode.VideoCodecNotSupport ->{
                    //视频解码格式不支持
                    SLog.e(PlayerEngine.TAG,"onInfo,video codec not support:${infoBean?.extraMsg}")
                }
                InfoCode.VideoDecoderDeviceError ->{
                    //视频解码器设备失败
                    SLog.e(PlayerEngine.TAG,"onInfo,video decoder device error:${infoBean?.extraMsg}")
                }
                InfoCode.VideoRenderInitError ->{
                    //视频渲染设备初始化失败
                    SLog.e(PlayerEngine.TAG,"onInfo,video render init error:${infoBean?.extraMsg}")
                }
                InfoCode.LowMemory ->{
                    //内存低
                    SLog.w(PlayerEngine.TAG,"onInfo,low memory:${infoBean?.extraMsg}")
                }
                InfoCode.CurrentPosition ->{
                    //当前播放位置
                    if(lsDuration==0L){
                        lsDuration = getDuration()
                    }
                    if(lsDuration!=0L){

                        infoBean?.let {
                            mCurrentPosition = it.extraValue
                            val progressPercent = (mCurrentPosition*100f/lsDuration).toInt()
                            callback?.onPlayProgress(progressPercent)
                            SLog.d(PlayerEngine.TAG,"onInfo,progress: $mCurrentPosition  $progressPercent%")
                        }

                    }else{
                        SLog.d(PlayerEngine.TAG,"onInfo,current position:  ${infoBean?.extraValue}")
                    }


                }
                else ->{
                    SLog.d(PlayerEngine.TAG, "onInfo,info = ${infoBean?.code} ${infoBean?.extraValue}  ${infoBean?.extraMsg}")
                }
            }
        }
        aliPlayer.setOnLoadingStatusListener(object :IPlayer.OnLoadingStatusListener{
            override fun onLoadingBegin() {
                SLog.i(PlayerEngine.TAG, "onBufferingBegin")
                callback?.onBufferingBegin()
            }

            override fun onLoadingProgress(p0: Int, p1: Float) {
                SLog.i(PlayerEngine.TAG, "onBufferingProgress $p0 $p1")
                callback?.onBufferingProgress(p0,p1)
            }

            override fun onLoadingEnd() {
                SLog.i(PlayerEngine.TAG, "onBufferingEnd")
                callback?.onBufferingEnd()
            }
        })
        aliPlayer.setOnSeekCompleteListener {
            SLog.i(PlayerEngine.TAG, "onSeekComplete")
            callback?.onSeekComplete()
        }

        aliPlayer.setOnStateChangedListener { p0 ->
            val state = PlayerState.convert(p0)
            if(currentState!=state){
                currentState = state
                SLog.i(PlayerEngine.TAG, "onStateChanged : $currentState")
                callback?.onStateChanged(currentState)
            }
        }
        aliPlayer.setOnSnapShotListener { p0, p1, p2 ->
            SLog.i(PlayerEngine.TAG, "onSnapShot,${p0?.hashCode()} $p1 $p2")
            callback?.onSnapShot(p0, p1, p2)
        }
    }


    override fun setDataSource(uri: String) {
        val urlSource = UrlSource()
        urlSource.uri = uri
        aliPlayer.setDataSource(urlSource)
        mUrl = uri
    }

    override fun prepare() {
        super.prepare()
        isLsAutoPlay = false
        aliPlayer.prepare()
    }

    override fun prepareAndStart() {
        super.prepare()
        isLsAutoPlay = true
        aliPlayer.prepare()
    }

    override fun start() {
        if(isPlaying()){
            return
        }
        aliPlayer.start()
    }

    override fun pause() {
       aliPlayer.pause()
    }

    override fun stop() {
        aliPlayer.stop()
    }

    override fun seekTo(position: Long) {
        aliPlayer.seekTo(position)
    }

    override fun reset() {
        SLog.i(PlayerEngine.TAG,"reset")
        aliPlayer.reset()
    }

    override fun release() {
        aliPlayer.release()
    }

    override fun reload() {
        aliPlayer.reload()
    }

    override fun isPlaying(): Boolean {
        return currentState==PlayerState.STARTED
    }


    override fun setLoop(isLoop:Boolean) {
        aliPlayer.isLoop = isLoop
    }

    override fun setDisplay(surface: SurfaceView) {
        SLog.i(PlayerEngine.TAG,"render : SurfaceView")
        surface.holder.addCallback(object :SurfaceHolder.Callback{
            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
                aliPlayer.redraw()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                aliPlayer.setDisplay(null)
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                aliPlayer.setDisplay(holder)
            }
        })
    }

    override fun setDisplay(texture: TextureView) {
        SLog.i(PlayerEngine.TAG,"render : TextureView")
        texture.surfaceTextureListener = object : TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                SLog.i(PlayerEngine.TAG,"onSurfaceTextureSizeChanged")
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                SLog.i(PlayerEngine.TAG,"onSurfaceTextureDestroyed")
                aliPlayer.setSurface(null)
                return true
            }

            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                SLog.i(PlayerEngine.TAG,"onSurfaceTextureAvailable")
                aliPlayer.setSurface(Surface(surface))
            }
        }
    }

    override fun setMute(isMute: Boolean) {
        aliPlayer.isMute = isMute
        SLog.i(PlayerEngine.TAG,"setMute : $isMute")
    }

    override fun isMute(): Boolean {
        return aliPlayer.isMute
    }

    override fun setScaleMode(mode: LsScaleMode) {
        aliPlayer.scaleMode = AliAdapter.transformScaleMode(mode)
    }

    override fun setVolume(volume: Float) {
        aliPlayer.volume = volume
    }

    override fun bindLifecycle(owner: LifecycleOwner?) {
        if(owner!=null){
            owner.lifecycle.addObserver(object :DefaultLifecycleObserver{
                override fun onDestroy(owner: LifecycleOwner) {
                    SLog.i(PlayerEngine.TAG,"lifecycle owner destroy, player release")
                    aliPlayer.release()
                }
            })
        }else{
            SLog.w(PlayerEngine.TAG,"bindLifecycle failed: owner is null")
        }
    }

    override fun getDuration(): Long {
        return aliPlayer.duration
    }

    override fun getCurrentPosition(): Long {
        return mCurrentPosition
    }
}