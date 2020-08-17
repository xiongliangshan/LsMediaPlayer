package com.xls.player.log

interface LogCallback{

    fun printMessage(level: LsLevel,message: String?)
}