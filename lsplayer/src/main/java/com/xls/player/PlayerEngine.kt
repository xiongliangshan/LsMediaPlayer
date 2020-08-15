package com.xls.player

import android.content.Context
import com.aliyun.player.nativeclass.PlayerConfig
import com.xls.player.ali.AliPlayerWrapper

class PlayerEngine {

    companion object{
        const val TAG = "PlayerEngine"
        fun createPlayer(context:Context,type:PlayerType = PlayerType.Ali):CommonPlayer{
            return when(type){
                PlayerType.Ali -> AliPlayerWrapper(context)
                else ->{
                    AliPlayerWrapper(context)
                }
            }
        }
    }

}