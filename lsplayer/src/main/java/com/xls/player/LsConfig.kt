package com.xls.player

class LsConfig{
    var isLoop:Boolean = false
    var cacheConfig:LsCacheConfig? = null
    var isAutoPlay:Boolean = false
    var netConfig:LsNetConfig? = null


    class Builder{
        private var isLoop:Boolean = false
        private var cacheConfig:LsCacheConfig? = null
        private var isAutoPlay:Boolean = false
        private var netConfig:LsNetConfig? = null


        fun setLoop(isLoop:Boolean):Builder{
            this.isLoop = isLoop
            return this
        }

        fun setCacheDir(cacheConfig:LsCacheConfig?):Builder{
            this.cacheConfig = cacheConfig
            return this
        }

        fun setAutoPlay(isAutoPlay:Boolean):Builder{
            this.isAutoPlay = isAutoPlay
            return this
        }

        fun setNetConfig(netConfig: LsNetConfig?):Builder{
            this.netConfig = netConfig
            return this
        }

        fun build():LsConfig{
            val lsConfig = LsConfig()
            lsConfig.isLoop = this.isLoop
            lsConfig.cacheConfig = this.cacheConfig
            lsConfig.isAutoPlay = this.isAutoPlay
            lsConfig.netConfig = this.netConfig
            return lsConfig
        }
    }

}