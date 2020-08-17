package com.xls.player

data class LsCacheConfig @JvmOverloads constructor(var dir:String,var maxSizeMB:Int = 100,var maxDurationS:Long = 60*60)
