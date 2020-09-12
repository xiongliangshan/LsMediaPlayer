#### 设计思路
- 使用外观模式给使用者提供简单便利的接口api，由使用者决定选择何种播放内核
- 通过建造者模式来构建播放配置
- 给所有播放内核抽象出统一的播放控制接口
- 给使用者提供统一的回调接口，中间做一层默认实现，让使用者按需使用
- 每种播放内核实现统一的播放控制接口和回调接口
- 每种播放内核用一个包装一层，用来实现一些逻辑适配统一接口



#### 集成方式


gradle集成

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```


```
dependencies {
	        implementation 'com.github.xiongliangshan:LsMediaPlayer:1.0.1'
	}
```


直接library依赖
把整个lsplayer模块拷贝到你的项目中作为一个libaray，然后

```
implementation project(':lsplayer')
```

#### 使用方式

###### 基本使用1（xml）

```
<com.xls.player.widget.LsVideoSurfaceView
        android:id="@+id/player"
        android:layout_width="0dp"
        android:layout_height="300dp"
        app:player_type="media"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintDimensionRatio="w,4:3"/>
```
或者使用TextureView渲染

```
<com.xls.player.widget.LsVideoTextureView
        android:id="@+id/player"
        android:layout_width="0dp"
        android:layout_height="300dp"
        app:player_type="media"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintDimensionRatio="w,4:3"/>
```
然后代码中开始播放

```
    player.setDataSource(url)
    player.prepareAndStart()
```





###### 基本使用2（使用自定义渲染视图）

```
var player: CommonPlayer? = null
player = PlayerEngine.createPlayer(context)
player.setDisplay(surfaceView)
player.setDataSource(url)
player.prepareAndStart()
```

###### 播放内核无缝切换
如果是使用xml方式，在LsVideoSurfaceView里有一个自定义属性player_type
（对应LsVideoTextureView里也是一样的），它的值是枚举类型，"ali" 和
"media",直接改这个值就可以实现切换播放内核，其他任何地方都不用改。

如果是代码动态创建播放器，默认就是使用ali播放器内核，可以传参选择

```
 fun createPlayer(context:Context,type:PlayerType = PlayerType.Ali):CommonPlayer{
            return when(type){
                PlayerType.Ali -> AliPlayerWrapper(context)
                PlayerType.Media -> MediaPlayerWrapper()
                else ->{
                    AliPlayerWrapper(context)
                }
            }
        }
```

###### 渲染器无缝切换
如果使用xml方式，直接把 LsVideoSurfaceView 和 LsVideoTextureView 相互替换即可。

如果是代码创建的播放器实例，直接更换方法参数

```
player.setDisplay(surfaceView)

player.setDisplay(textureView)
```

###### 绑定生命周期
在Activity或者Fragment中使用播放器的时候，可以调用如下api

```
player?.bindLifecycle(owner)
```
这样就不用关心播放器资源释放的问题了，会在onDestroy的时候自动帮你释放播放器资源

###### 设置回调监听

```
player.callback = object :SimpleLsPlayerCallback(){
                override fun onVideoSizeChanged(width: Int, height: Int) {
                    super.onVideoSizeChanged(width, height)
                }

                override fun onRenderingStart() {
                    super.onRenderingStart()
                }

                override fun onInfo(info: LsInfo?) {
                    super.onInfo(info)
                }

                override fun onSnapShot(bm: Bitmap?, with: Int, height: Int) {
                    super.onSnapShot(bm, with, height)
                }

                override fun onPrepared() {
                    super.onPrepared()
                }

                override fun onCompletion() {
                    super.onCompletion()
                }

                override fun onError(info: LsInfo) {
                    super.onError(info)
                }

                override fun onBufferingBegin() {
                    super.onBufferingBegin()
                }

                override fun onBufferingProgress(percent: Int, kbps: Float) {
                    super.onBufferingProgress(percent, kbps)
                }

                override fun onBufferingEnd() {
                    super.onBufferingEnd()
                }

                override fun onSeekComplete() {
                    super.onSeekComplete()
                }

                override fun onStateChanged(state: PlayerState) {
                    super.onStateChanged(state)
                }

                override fun onFetchDurationFinished(duration: Long) {
                    super.onFetchDurationFinished(duration)
                }

                override fun onPlayProgress(percent: Int) {
                    super.onPlayProgress(percent)
                }
            }
```
这些方法名字取得比较形象，一般能看出来是什么意思，如果实在看不出来，从代码点击调到顶层接口定义的地方，每个方法都有注释。


###### 设置缓存

```
player?.lsConfig  = LsConfig.Builder()
                .setLoop(true)
                .setCacheDir(LsCacheConfig(it.cacheDir.absolutePath+File.separator+"video"))
                .setNetConfig(LsNetConfig(5000,2))
                .build()
```
目前setCacheDir  和 setNetConfig，这个只有在使用ali播放内核的时候才有效。


###### 日志监控
在整个播放器封装的关键地方都打印了日志，内部采用原生的Log。

```
object SLog{

    var switch = true
    var printer:LogCallback? = null


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



    fun e(tag:String?,message: String?){
        if(switch){
            Log.e(tag,message)
        }
        printer?.printMessage(LsLevel.E,message)
    }

}
```
一般的信息都用的info级别，打印非常频范的日志使用的是debug级别，错误但不致命的使用warn级别，错误并且中断播放的使用error级别。

这里设了一个开关 swtich 默认是打开的，可以根据项目不同环境的包配置打开后者关闭。

这里还有一个LogCallback，主要是把日志回调抛出来给使用者。可以很方便的做存储或者网络上传到服务器。


###### 快速开始
如果只是简单的播放一个视频，可以使用LsVideoSurfaceView 和 LsVideoTextureView 的 fastStart方法，一句代码实现播放

```
 player?.fastStart(url,object :SimpleLsPlayerCallback(){

            })
```


#### 总结：
本次播放器封装主要是解决项目中零散的播放音视频需求，让使用者更方便的切换播放器内核，容易扩展，使用起来更加方便，容易调试。目前支持阿里云播放器和MediaPlayer。有些功能统一接口可能没有暴露出来，待后续慢慢完善
