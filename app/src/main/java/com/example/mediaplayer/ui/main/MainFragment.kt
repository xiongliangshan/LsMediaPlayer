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

                override fun onFetchDurationFinished(duration: Long) {
                    totalTime.text = formatTime(duration)
                }
                override fun onError(info: LsInfo) {

                }

                override fun onBufferingBegin() {
                    progressBar?.visibility = View.VISIBLE
                }

                override fun onBufferingEnd() {
                    progressBar?.visibility = View.GONE
                }

                override fun onPlayProgress(currentPos: Long, percent: Int) {
                    if(!isTracking){
                        seekBar?.progress = percent
                    }
                    currentTime.text = formatTime(currentPos)
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
                    currentTime.text = formatTime(currentPos)
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

    private fun formatTime(time:Long):String{
         val second = time/1000
         var secondStr = "00"
         var minuteStr = "00"
         var hourStr = "00"
         when(second){
            in 0 until 60 ->{
                secondStr = if(second <10){
                    "0$second"
                }else{
                    second.toString()
                }
            }
            in 60 until 60*60 ->{
                val minute = second/60
                minuteStr = if(minute<10){
                    "0$minute"
                }else{
                    minute.toString()
                }
                val s  = second%60
                secondStr = if(s<10){
                    "0$s"
                }else{
                    s.toString()
                }
            }
            else ->{
                val hour = second/60*60
                hourStr = if(hour<10){
                    "0$hour"
                }else{
                    hour.toString()
                }
                val m = second%3600/60
                minuteStr = if(m<10){
                    "0$m"
                }else{
                    m.toString()
                }
                val s = second%3600%60
                secondStr = if(s<10){
                    "0$s"
                }else{
                    s.toString()
                }
            }
        }
        return "$hourStr : $minuteStr : $secondStr"
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