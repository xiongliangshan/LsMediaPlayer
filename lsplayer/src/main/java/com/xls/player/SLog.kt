package com.xls.player

import android.util.Log

object SLog{

    var switch = true

    fun v(tag:String?,message:String?){
        if(switch){
            Log.v(tag,message)
        }
    }

    fun d(tag: String?,message: String?){
        if(switch){
            Log.d(tag,message)
        }
    }

    fun i(tag:String?,message: String?){
        if(switch){
            Log.i(tag,message)
        }
    }

    fun w(tag:String?,message: String?){
        if(switch){
            Log.w(tag,message)
        }
    }

    fun e(tag:String?,message: String?){
        if(switch){
            Log.e(tag,message)
        }
    }


}