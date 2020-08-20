package com.xls.player.widget

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView
import androidx.lifecycle.LifecycleOwner
import com.example.lib_player.R
import com.xls.player.*
import com.xls.player.log.SLog
import java.util.logging.Logger

class LsVideoSurfaceView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), ILsPlayer {

    private lateinit var player: CommonPlayer
    var currentState: PlayerState = PlayerState.IDLE
        private set
        get() = player.currentState

    var source: String? = null
        private set
        get() = player.mUrl

    var lsConfig: LsConfig? = null
        set(value) {
            field = value
            player.lsConfig = value
        }

    var callback: LsPlayerCallback? = null
        set(value) {
            field = value
            player.callback = value
        }
    private var playerValue: Int = PlayerType.Ali.ordinal

    init {

        context?.run {
            attrs.let {
                val typedArray = context.obtainStyledAttributes(it, R.styleable.LsVideoSurfaceView)
                playerValue = typedArray.getInt(R.styleable.LsVideoSurfaceView_s_player_type, 1)
                typedArray.recycle()
            }
            player = PlayerEngine.createPlayer(this, PlayerType.convert(playerValue))
        }

        bindRender()
    }

    private fun bindRender() {
        player.setDisplay(this)
    }

    override fun setDataSource(uri: String) {
        player.setDataSource(uri)
    }

    override fun prepare() {
        player.prepare()
    }

    override fun prepareAndStart() {
        player.prepareAndStart()
    }

    override fun start() {
        player.start()
    }

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.stop()
    }

    override fun seekTo(position: Long) {
        player.seekTo(position)
    }

    override fun reset() {
        player.reset()
    }

    override fun release() {
        player.release()
    }

    override fun setMute(isMute: Boolean) {
        player.setMute(isMute)
    }

    override fun isMute(): Boolean {
        return player.isMute()
    }

    override fun setLoop(isLoop: Boolean) {
        player.setLoop(isLoop)
    }

    override fun reload() {
        player.reload()
    }

    override fun isPlaying(): Boolean {
        return player.isPlaying()
    }

    override fun bindLifecycle(owner: LifecycleOwner?) {
        player.bindLifecycle(owner)
    }


    override fun setScaleMode(mode: LsScaleMode) {
        player.setScaleMode(mode)
    }

    override fun setVolume(volume: Float) {
        player.setVolume(volume)
    }

    override fun getDuration(): Long {
        return player.getDuration()
    }

    override fun getCurrentPosition(): Long {
        return player.getCurrentPosition()
    }
}