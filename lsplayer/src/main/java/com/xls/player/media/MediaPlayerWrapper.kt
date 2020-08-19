package com.xls.player.media

import android.content.Context
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

class MediaPlayerWrapper(private val context: Context) :CommonPlayer(){

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var mIsMute = false
    private var mVolume = 1f

    init {
        setListeners()
        SLog.i(PlayerEngine.TAG,"create player instance : MediaPlayer")
    }

    private fun setListeners() {
        mediaPlayer.setOnPreparedListener {
            currentState = PlayerState.PREPARED
            SLog.i(PlayerEngine.TAG,"onPrepared, url = $mUrl duration = $lsDuration ms")
            callback?.onPrepared()
            lsDuration = getDuration()
            callback?.onFetchDurationFinished(lsDuration)
            if(isLsAutoPlay){
                it.start()
            }
        }
        mediaPlayer.setOnCompletionListener {
            currentState = PlayerState.COMPLETION
            SLog.i(PlayerEngine.TAG,"onCompletion")
            callback?.onCompletion()
        }
        mediaPlayer.setOnErrorListener { _, what, extra ->
            currentState = PlayerState.ERROR
            callback?.onError(LsInfo(what,"",extra.toString()))
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
            }
            SLog.i(PlayerEngine.TAG, "onInfo,$what $extra")
            true
        }
        mediaPlayer.setOnBufferingUpdateListener { _, percent ->
            SLog.i(PlayerEngine.TAG, "onBufferingProgress $percent ")
            callback?.onBufferingProgress(percent, 0f)
        }
        mediaPlayer.setOnSeekCompleteListener {
            SLog.i(PlayerEngine.TAG, "onSeekComplete")
            callback?.onSeekComplete()
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
        currentState = PlayerState.INITALIZED
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
        mediaPlayer.start()
        currentState = PlayerState.STARTED
    }

    override fun pause() {
        mediaPlayer.pause()
        currentState = PlayerState.PAUSED
    }

    override fun stop() {
        mediaPlayer.stop()
        currentState = PlayerState.STOPPED
    }

    override fun seekTo(position: Long) {
        mediaPlayer.seekTo(position.toInt())
    }

    override fun reset() {
        mediaPlayer.reset()
        currentState = PlayerState.IDLE
    }

    override fun release() {
        mediaPlayer.release()
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
                    mediaPlayer.release()
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
        config?.let {
            mediaPlayer.isLooping = it.isLoop
        }
    }

    override fun setScaleMode(mode: LsScaleMode) {
        mediaPlayer.setVideoScalingMode(MediaAdapter.transformScaleMode(mode))
    }

    override fun setVolume(volume: Float) {
        mediaPlayer.setVolume(volume,volume)
        mVolume = volume
    }

    override fun getDuration(): Long {
        return mediaPlayer.duration.toLong()
    }
}