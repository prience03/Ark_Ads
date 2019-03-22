# Ark_Ads
聚合广告sdk
## 使用方法
### Gradle
```groovy
maven { url 'http://nexus.xiaoc.cn/repository/maven-releases/'}

implementation 'com.ark.ads:imageview:1.0.0'
implementation 'com.ark.ads:polymers:1.0.0'
implementation 'com.ark.ads:plttad:1.0.0'
implementation 'com.ark.ads:pllongyun:1.0.0'
implementation 'com.ark.ads:plzhaocai:1.0.0'
implementation 'com.ark.ads:plgdt:1.0.0'
implementation 'com.ark.ads:plifly:1.0.0'
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
##### 4.交叉推广
```java
new SelfNativeAD(this, SelfADStyle.INTER_RECOMMEND)
        .setListener(new SelfNativeAD.ADListener() {
                @Override
                public void onAdLoad(List<SelfDataRef> dataRefs) {
                        if (dataRefs != null && dataRefs.size() > 0) {
                            SelfDataRef selfDataRef = dataRefs.get(0);
                            RecommendDialog recommendDialog = new RecommendDialog(
                                    context);
                            recommendDialog.setBundle(selfDataRef);
                            recommendDialog.show();
                        }
                    }

                @Override
                public void onAdFailed(int errorCode, @NonNull String errorMsg) {

                }
        }).loadAllADList();
```
##### 5.可能出现的冲突解决
```xml
<activity
            android:name="com.baidu.mobads.AppActivity"
            tools:replace="android:configChanges"
            android:configChanges="keyboard|keyboardHidden|orientation"/>
```