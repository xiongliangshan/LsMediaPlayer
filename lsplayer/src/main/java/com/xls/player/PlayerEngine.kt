package com.xls.player

import android.content.Context
import com.aliyun.player.nativeclass.PlayerConfig
import com.xls.player.ali.AliPlayerWrapper
import com.xls.player.media.MediaPlayerWrapper

class PlayerEngine {

    companion object{
        const val TAG = "LsPlayer"
        fun createPlayer(context:Context,type:PlayerType = PlayerType.Ali):CommonPlayer{
            return when(type){
                PlayerType.Ali -> AliPlayerWrapper(context)
                PlayerType.Media -> MediaPlayerWrapper(context)
                else ->{
                    AliPlayerWrapper(context)
                }
            }
        }
    }

}