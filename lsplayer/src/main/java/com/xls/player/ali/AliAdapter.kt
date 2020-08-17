package com.xls.player.ali

import com.aliyun.player.IPlayer
import com.aliyun.player.nativeclass.CacheConfig
import com.xls.player.LsCacheConfig
import com.xls.player.LsScaleMode
import com.xls.player.PlayerEngine
import com.xls.player.log.SLog

object AliAdapter {

    @JvmStatic
    fun transformCacheConfig(lsCacheConfig: LsCacheConfig): CacheConfig {
        return CacheConfig().apply {
            this.mEnable = true
            this.mDir = lsCacheConfig.dir
            SLog.i(PlayerEngine.TAG, "mDir = $mDir")
            this.mMaxDurationS = lsCacheConfig.maxDurationS
            this.mMaxSizeMB = lsCacheConfig.maxSizeMB
        }
    }

    @JvmStatic
    fun transformScaleMode(mode: LsScaleMode): IPlayer.ScaleMode {
        return when (mode) {
            LsScaleMode.SCALE_ASPECT_FILL -> IPlayer.ScaleMode.SCALE_ASPECT_FILL
            LsScaleMode.SCALE_ASPECT_FIT -> IPlayer.ScaleMode.SCALE_ASPECT_FIT
            LsScaleMode.SCALE_TO_FILL -> IPlayer.ScaleMode.SCALE_TO_FILL
            else -> IPlayer.ScaleMode.SCALE_ASPECT_FIT
        }
    }
}