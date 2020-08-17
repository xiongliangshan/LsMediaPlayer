package com.xls.player

object PlayerConstants{



}


enum class PlayerType(val value: Int) {
    Ali(1), Media(2);


    companion object {

        @JvmStatic
        fun convert(value: Int): PlayerType {
            return when (value) {
                1 -> Ali
                2 -> Media
                else -> Ali
            }
        }
    }
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
    UNKNOW(-1);

    companion object{
        @JvmStatic
        fun convert(value:Int):PlayerState{
            return when(value){
                0 -> IDLE
                1 -> INITALIZED
                2 -> PREPARED
                3 -> STARTED
                4 -> PAUSED
                5 -> STOPPED
                6 -> COMPLETION
                7 -> ERROR
                else -> UNKNOW
            }
        }
    }

}


enum class LsScaleMode(val value: Int){

    /**
     * 宽高比填充
     */
    SCALE_ASPECT_FILL(0),

    /**
     * 宽高比适应
     */
    SCALE_ASPECT_FIT(1),

    /**
     * 拉伸填充
     */
    SCALE_TO_FILL(2);
}



