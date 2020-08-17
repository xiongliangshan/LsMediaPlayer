package com.xls.player

/**
 * networkTimeout 网络超时时间，单位ms
 * networkRetryCount 超时后重试的次数
 */
data class LsNetConfig @JvmOverloads constructor(var networkTimeout:Int=5000,var networkRetryCount:Int=2)