package com.xls.player

object PlayerConstants{



}


enum class PlayerType(val value:Int){
    Ali(1),Media(2)
}

enum class PlayerState(val value:Int){
    IDLE(0),
    INITALIZED(1),
    PREPARED(2),
    STARTED(3),
    PAUSED(4),
    STOPPED(5),
    COMPLETION(6),
    ERROR(7),
    UNKNOW(-1)

}

fun convert(value:Int):PlayerState{
    return when(value){
        0 -> PlayerState.IDLE
        1 -> PlayerState.INITALIZED
        2 -> PlayerState.PREPARED
        3 -> PlayerState.STARTED
        4 -> PlayerState.PAUSED
        5 -> PlayerState.STOPPED
        6 -> PlayerState.COMPLETION
        7 -> PlayerState.ERROR
        else -> PlayerState.UNKNOW
    }
}

