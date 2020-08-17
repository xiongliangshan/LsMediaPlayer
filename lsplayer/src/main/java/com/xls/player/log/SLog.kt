package com.xls.player.log

import android.util.Log

object SLog{

    var switch = true
    var printer:LogCallback? = null

    fun v(tag:String?,message:String?){
        if(switch){
            Log.v(tag,message)
        }
        printer?.printMessage(LsLevel.V,message)
    }

    fun d(tag: String?,message: String?){
        if(switch){
            Log.d(tag,message)
        }
        printer?.printMessage(LsLevel.D,message)
    }

    fun i(tag:String?,message: String?){
        if(switch){
            Log.i(tag,message)
        }
        printer?.printMessage(LsLevel.I,message)
    }

    fun w(tag:String?,message: String?){
        if(switch){
            Log.w(tag,message)
        }
        printer?.printMessage(LsLevel.W,message)
    }

    fun e(tag:String?,message: String?){
        if(switch){
            Log.e(tag,message)
        }
        printer?.printMessage(LsLevel.E,message)
    }


}