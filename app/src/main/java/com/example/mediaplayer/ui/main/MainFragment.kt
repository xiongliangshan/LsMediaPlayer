package com.example.mediaplayer.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aliyun.player.bean.ErrorInfo
import com.example.mediaplayer.R
import com.xls.player.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.io.File

class MainFragment : Fragment(),View.OnClickListener {

    var player: CommonPlayer? = null
    private var degree = 30f
    private var scaleFactor = 0.3f
    private var direction = 0

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
            player = PlayerEngine.createPlayer(it)
            player?.lsConfig  = LsConfig.Builder()
                .setLoop(true)
                .setCacheDir(LsCacheConfig(it.cacheDir.absolutePath+File.separator+"video"))
                .setAutoPlay(true)
                .build()
            player?.bindLifecycle(MainFragment@this)
            player?.callback = object :SimpleLsPlayerCallback(){
                override fun onError(errorInfo: ErrorInfo) {
                    super.onError(errorInfo)
                }
            }
            player?.setDisplay(lsTextureView)
            player?.setDataSource(url)
            player?.prepare()
        }
        btnStart.setOnClickListener(this)
        btnPause.setOnClickListener(this)
        btnReset.setOnClickListener(this)
        btnMute.setOnClickListener(this)
        btnRotate.setOnClickListener(this)
        btnScale.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnStart ->{
                player?.start()
            }
            R.id.btnPause ->{
                player?.pause()
            }
            R.id.btnReset ->{
                player?.reset()
                player?.prepare()
            }
            R.id.btnMute ->{
                player?.let {
                    it.setMute(!it.isMute())
                }
            }
            R.id.btnRotate ->{
                lsTextureView.rotation = lsTextureView.rotation+degree
            }
            R.id.btnScale ->{
                if(direction==0){
                    lsTextureView.scaleX = lsTextureView.scaleX+scaleFactor
                    lsTextureView.scaleY = lsTextureView.scaleY+scaleFactor
                    if( lsTextureView.scaleY>3){
                        direction = 1
                    }
                }else {
                    lsTextureView.scaleX = lsTextureView.scaleX-scaleFactor
                    lsTextureView.scaleY = lsTextureView.scaleY-scaleFactor
                    if(lsTextureView.scaleX<0.3){
                        direction = 0
                    }
                }
            }
        }
    }
}