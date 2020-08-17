package com.xls.player.media

import com.xls.player.LsErrorInfo

object MediaAdapter {

    fun transformErrorInfo(what:Int,extra:Int):LsErrorInfo{
        // TODO: 2020/8/17
        var code = 0
        var message = ""
        var extra = ""
        return LsErrorInfo(code,message,extra)
    }
}