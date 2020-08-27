package com.example.mediaplayer.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mediaplayer.R
import com.xls.player.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.io.File

class MainFragment : Fragment(),View.OnClickListener {

//    var player: CommonPlayer? = null
    private var degree = 30f
    private var scaleFactor = 0.3f
    private var direction = 0
    private var isTracking = false

    companion object {
        fun newInstance() = MainFragment()

        const val url = "https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218093206z8V1JuPlpe.mp4"
        const val url1 = "http://stream.iqilu.com/vod_bag_2016//2020/02/16/903BE158056C44fcA9524B118A5BF230/903BE158056C44fcA9524B118A5BF230_H264_mp4_500K.mp4"



    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        context?.let {
            player?.lsConfig  = LsConfig.Builder()
                .setLoop(true)
                .setCacheDir(LsCacheConfig(it.cacheDir.absolutePath+File.separator+"video"))
                .setNetConfig(LsNetConfig(5000,2))
                .build()
            player?.bindLifecycle(MainFragment@this)
            player?.callback = object :SimpleLsPlayerCallback(){
                override fun onError(info: LsInfo) {

                }

                override fun onBufferingBegin() {
                    progressBar?.visibility = View.VISIBLE
                }

                override fun onBufferingEnd() {
                    progressBar?.visibility = View.GONE
                }

                override fun onPlayProgress(percent: Int) {
                    if(!isTracking){
                        seekBar?.progress = percent
                    }
                }
            }
//            player?.setDisplay(player)
            player?.setDataSource(url1)
            player?.prepareAndStart()

        }
        btnStart.setOnClickListener(this)
        btnPause.setOnClickListener(this)
        btnStop.setOnClickListener(this)
        btnReset.setOnClickListener(this)
        btnMute.setOnClickListener(this)
        btnRotate.setOnClickListener(this)
        btnScale.setOnClickListener(this)
        seekBar?.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(isTracking){
                    val currentPos = (player.getDuration()*progress/100f).toLong()
                    player.seekTo(currentPos)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isTracking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isTracking = false
            }
        })
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnStart ->{
                when (player.currentState) {
                    PlayerState.PREPARED,
                    PlayerState.PAUSED -> player?.start()
                    PlayerState.STOPPED -> {
                        player.prepareAndStart()
                    }
                }
            }
            R.id.btnPause ->{
                player?.pause()
            }
            R.id.btnStop ->{
                player?.stop()
            }
            R.id.btnReset ->{
                player?.reset()
                player.setDataSource(url)
                player?.prepareAndStart()
            }
            R.id.btnMute ->{
                player?.let {
                    it.setMute(!it.isMute())
                }
            }
            R.id.btnRotate ->{
                player.rotation = player.rotation + degree
            }
            R.id.btnScale ->{
                if(direction==0){
                    player.scaleX = player.scaleX+scaleFactor
                    player.scaleY = player.scaleY+scaleFactor
                    if( player.scaleY>3){
                        direction = 1
                    }
                }else {
                    player.scaleX = player.scaleX-scaleFactor
                    player.scaleY = player.scaleY-scaleFactor
                    if(player.scaleX<0.3){
                        direction = 0
                    }
                }
            }
        }
    }


}