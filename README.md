# Ark_Ads
聚合广告sdk
## 使用方法
### Gradle
```groovy
maven { url "https://dl.bintray.com/cherish/maven" }

implementation 'com.github.xslczx:basics:1.2.4'
implementation 'com.github.xslczx:polymers:1.2.4'
implementation 'com.github.xslczx:plifly:1.2.4'
implementation 'com.github.xslczx:pllongyun:1.2.4'
implementation 'com.github.xslczx:plzhaocai:1.2.4'
implementation 'com.github.xslczx:plttad:1.2.4'
implementation 'com.github.xslczx:plgdt:1.2.4'
```
### 初始化SDK，非必须
```java
ADTool.initialize(new ADTool.Builder()
        .setStrategy(Strategy.cycle)//按sort轮流排序策略
        .setLoadOtherWhenVideoDisable(true)//视频广告失败后尝试其他原生横图广告填充
        .setLocalConfig(JsonUtils.getJson(this,"localconfig.json"))//使用本地json字符串配置
        .setDebugMode(BuildConfig.DEBUG)//调试模式（日志打印，平台标识）
        .build());
```
#### 广告使用
##### 1.开屏
```java
ADTool.getADTool().getManager()
        .getSplashWrapper()
        .loadSplash(activity, adContainer, rootView, new OnSplashImpl() {

                @Override
                public void onAdTimeTick(long tick) {

                }

                @Override
                public void onAdShouldLaunch() {

                }
        });
```
##### 2.原生信息流
```java
ADTool.getADTool().getManager()
        .getWrapper()
        .loadBannerView(context, adContainer);
        
ADTool.getADTool().getManager()
        .getNativeWrapper()
        .loadNativeView(context, adContainer);
        
ADTool.getADTool().getManager()
        .getNativeWrapper()
        .loadSmallNativeView(context, adContainer);
        
ADTool.getADTool().getManager()
        .getNativeWrapper()
        .loadVideoView(context, adContainer);
```
##### 3.配置
```java
Map<String,String> mConfig= ADTool.getADTool()
        .getManager()
        .getConfigWrapper()
        .getConfig();
        
boolean hasAd=ADTool.getADTool()
        .getManager()
        .getConfigWrapper()
        .hasAd();
```
