package com.xls.player.media

import android.graphics.SurfaceTexture
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.xls.player.*
import com.xls.player.log.SLog
import java.util.*

class MediaPlayerWrapper :CommonPlayer(){

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var mIsMute = false
    private var mVolume = 1f
    private var mTimer:Timer? = null
    private var progressPercent = 0

    companion object{
        const val TIMER_PERIOD = 500L
        const val MAX_PROGRESS = 100
    }


    init {
        setListeners()
        SLog.i(PlayerEngine.TAG,"create player instance : MediaPlayer")
    }


    private fun startTimer(){
        if(mTimer==null){
            mTimer = Timer()
        }
        mTimer?.scheduleAtFixedRate(LsTimerTask(),0,TIMER_PERIOD)
    }

    private fun cancelTimer(){
        mTimer?.cancel()
        mTimer?.purge()
        mTimer = null
    }

    private fun setListeners() {
        mediaPlayer.setOnPreparedListener {
            setState(PlayerState.PREPARED)
            SLog.i(PlayerEngine.TAG,"onPrepared, url = $mUrl duration = $lsDuration ms")
            callback?.onPrepared()
            lsDuration = getDuration()
            callback?.onFetchDurationFinished(lsDuration)
            if(isLsAutoPlay){
                start()
            }
        }
        mediaPlayer.setOnCompletionListener {
            setState(PlayerState.COMPLETION)
            SLog.i(PlayerEngine.TAG,"onCompletion")
            cancelTimer()
            if (progressPercent < MAX_PROGRESS) {
                progressPercent = MAX_PROGRESS
                callback?.onPlayProgress(progressPercent)
                SLog.i(PlayerEngine.TAG, "onInfo,progress:  $progressPercent")
            }
            callback?.onCompletion()
        }
        mediaPlayer.setOnErrorListener { _, what, extra ->
            setState(PlayerState.ERROR)
            cancelTimer()
            callback?.onError(LsInfo(what,"",extra.toString()))
            SLog.e(PlayerEngine.TAG,"onError $what | $extra")
            true
        }
        mediaPlayer.setOnVideoSizeChangedListener { _, width, height ->
            SLog.i(PlayerEngine.TAG, "onVideoSizeChanged,width = $width height = $height")
            callback?.onVideoSizeChanged(width, height)
        }
        mediaPlayer.setOnInfoListener { _, what, extra ->
            callback?.onInfo(MediaAdapter.transformInfo(what,extra))
            when(what){
                MediaPlayer.MEDIA_INFO_BUFFERING_START ->{
                    //开始缓冲
                    callback?.onBufferingBegin()
                }
                MediaPlayer.MEDIA_INFO_BUFFERING_END ->{
                    //结束缓冲
                    callback?.onBufferingEnd()
                }
                MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START ->{
                    //视频开始渲染
                    callback?.onRenderingStart()
                }
                MediaPlayer.MEDIA_ERROR_IO ->{
                    //文件不存在或者网络IO错误
                    SLog.e(PlayerEngine.TAG,"onInfo,file or network related operation errors,$what $extra")
                }
                MediaPlayer.MEDIA_ERROR_MALFORMED ->{
                    SLog.e(PlayerEngine.TAG,"onInfo,bitstream is not conforming to the related coding standard or file spec,$what $extra")
                }
                MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK ->{
                    SLog.e(PlayerEngine.TAG,"onInfo,media_error_not_valid_for_progressive_playback,$what $extra")
                }
                MediaPlayer.MEDIA_ERROR_SERVER_DIED ->{
                    SLog.e(PlayerEngine.TAG,"onInfo,media_error_server_died,$what $extra")
                }
                MediaPlayer.MEDIA_ERROR_TIMED_OUT ->{
                    SLog.e(PlayerEngine.TAG,"onInfo,media_error_timed_out,$what $extra")
                }
                MediaPlayer.MEDIA_ERROR_UNKNOWN ->{
                    SLog.e(PlayerEngine.TAG,"onInfo,media_error_unknown,$what $extra")
                }
                MediaPlayer.MEDIA_ERROR_UNSUPPORTED ->{
                    SLog.e(PlayerEngine.TAG,"onInfo,media_error_unsupported,$what $extra")
                }
                else ->{
                    SLog.i(PlayerEngine.TAG, "onInfo,$what $extra")
                }
            }
            true
        }
        mediaPlayer.setOnBufferingUpdateListener { _, percent ->
            SLog.d(PlayerEngine.TAG, "onBufferingProgress $percent ")
            callback?.onBufferingProgress(percent, 0f)
        }
        mediaPlayer.setOnSeekCompleteListener {
            SLog.i(PlayerEngine.TAG, "onSeekComplete")
            callback?.onSeekComplete()
            startTimer()
        }
    }


    override fun setDisplay(surface: SurfaceView) {
        SLog.i(PlayerEngine.TAG,"render : SurfaceView")
        surface.holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                mediaPlayer.setDisplay(null)
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                mediaPlayer.setDisplay(holder)
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
                mediaPlayer.setSurface(null)
                return true
            }

            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture?,
                width: Int,
                height: Int
            ) {
                SLog.i(PlayerEngine.TAG,"onSurfaceTextureAvailable")
                mediaPlayer.setSurface(Surface(surface))
            }
        }
    }

    override fun setDataSource(uri: String) {
        mediaPlayer.setDataSource(uri)
        setState(PlayerState.INITALIZED)
        mUrl = uri
    }

    override fun prepare() {
        super.prepare()
        isLsAutoPlay = false
        mediaPlayer.prepareAsync()
    }

    override fun prepareAndStart() {
        super.prepare()
        isLsAutoPlay = true
        mediaPlayer.prepareAsync()
    }

    override fun start() {
        if(isPlaying()){
            return
        }
        mediaPlayer.start()
        setState(PlayerState.STARTED)
        startTimer()
    }

    override fun pause() {
        mediaPlayer.pause()
        setState(PlayerState.PAUSED)
        cancelTimer()
    }

    override fun stop() {
        mediaPlayer.stop()
        setState(PlayerState.STOPPED)
        cancelTimer()
    }

    override fun seekTo(position: Long) {
        cancelTimer()
        mediaPlayer.seekTo(position.toInt())
    }

    override fun reset() {
        mediaPlayer.reset()
        setState(PlayerState.IDLE)
        cancelTimer()
    }

    override fun release() {
        mediaPlayer.release()
        cancelTimer()
    }

    override fun setMute(isMute: Boolean) {
        if(isMute){
            setVolume(0f)
        }else{
            setVolume(mVolume)
        }
        mIsMute = isMute
    }

    override fun isMute(): Boolean {
        return mIsMute
    }

    override fun setLoop(isLoop: Boolean) {
        mediaPlayer.isLooping = isLoop
    }

    override fun reload() {

    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun bindLifecycle(owner: LifecycleOwner?) {
        if(owner!=null){
            owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    SLog.i(PlayerEngine.TAG,"lifecycle owner destroy, player release")
                    release()
                }
            })
        }else{
            SLog.w(PlayerEngine.TAG,"bindLifecycle failed: owner is null")
        }
    }

    override fun config(config: LsConfig?) {
        SLog.i(PlayerEngine.TAG,"config:$config")
        mediaPlayer.setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
        mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        mediaPlayer.isLooping = false
        config?.let {
            mediaPlayer.isLooping = it.isLoop
        }
    }

    override fun setScaleMode(mode: LsScaleMode) {
        mediaPlayer.setVideoScalingMode(MediaAdapter.transformScaleMode(mode))
    }

    override fun setVolume(volume: Float) {
        mediaPlayer.setVolume(volume,volume)
        if(volume!=0f){
            mVolume = volume
        }
    }

    override fun getDuration(): Long {
        return mediaPlayer.duration.toLong()
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

    private fun setState(state:PlayerState){
        if(currentState!=state){
            currentState = state
            SLog.i(PlayerEngine.TAG, "onStateChanged : $currentState")
            callback?.onStateChanged(state)
        }

    }

    inner class LsTimerTask:TimerTask(){
        override fun run() {
            //当前播放位置
            if (lsDuration == 0L) {
                lsDuration = getDuration()
            }
            val currentPos = getCurrentPosition()
            if (lsDuration != 0L) {
                progressPercent = (currentPos * 100f / lsDuration).toInt()
                callback?.onPlayProgress(progressPercent)
                SLog.d(PlayerEngine.TAG, "onInfo,progress:  $currentPos  $progressPercent%")
            } else {
                SLog.d(PlayerEngine.TAG, "onInfo,current position: $currentPos")
            }
        }
    }
}